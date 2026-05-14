package com.example.mes.producao.application.facade;


import com.example.mes.producao.api.exception.*;
import com.example.mes.producao.application.dto.*;
import com.example.mes.producao.application.mapper.ProgramacaoMapper;
import com.example.mes.producao.application.service.*;
import com.example.mes.producao.application.service.strategy.programacao.MudancaStatusStrategy;
import com.example.mes.producao.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;

@Service
public class ProducaoFacade {

        private final LoteService loteService;
        private final OrdemProducaoService ordemService;
        private final EquipamentoService equipamentoService;
        private final ProgramacaoService programacaoService;
        private final Map<StatusProgramacao, MudancaStatusStrategy> estrategias = new EnumMap<>(StatusProgramacao.class);
        private final ProgramacaoMapper programacaoMapper;
        private final RastreabilidadeService rastreabilidadeService;



    public ProducaoFacade(List<MudancaStatusStrategy> listaEstrategias, LoteService loteService, OrdemProducaoService ordemService,
                          EquipamentoService equipamentoService, ProgramacaoService programacaoService, ProgramacaoMapper programacaoMapper, RastreabilidadeService rastreabilidadeService) {


        this.loteService = loteService;
        this.programacaoMapper = programacaoMapper;
        this.programacaoService = programacaoService;
        this.equipamentoService = equipamentoService;
        this.ordemService = ordemService;
        this.rastreabilidadeService = rastreabilidadeService;
     for (MudancaStatusStrategy estrategia : listaEstrategias) {
        this.estrategias.put(estrategia.getStatusAlvo(), estrategia);
    }
    

}


    @Transactional
    public OrdemProducao vincularOrdemProducaoAoLote( Long idProd,Long idLote) {

       Lote lote =  loteService.buscarLotePorId(idLote);

        
        return ordemService.vincularLote(idProd, lote);
    }



    @Transactional
    public ProgramacaoResponseDTO criarProgramaDoLote(ProgramacaoRequestDTO programacaoRequestDTO) {
        Equipamento equipamento = equipamentoService.buscarEquipamentoPorId(programacaoRequestDTO.equipamentoId());

        Lote lote = loteService.buscarLotePorId(programacaoRequestDTO.loteId());

        if(programacaoService.existirProgamaPorLoteId(lote.getId())){
            throw new NotProgramacaoValidException("Esse lote " + lote.getNome()+ " já possui programa");

        }
        if (equipamento.getStatusEquipamento() == StatusEquipamento.PARADO) {
            throw new NotProgramacaoValidException("Não é possível programar: O equipamento está parado.");
        }
        if(lote.getOrdemProducao()== null){
            throw new NotProgramacaoValidException("O lote " + lote.getNome() + " não possui ordem de produção, portanto não pode ser programado");

        }

        Programacao programacao = programacaoMapper.toEntity(lote,equipamento,programacaoRequestDTO);

        Integer ultimaFila = programacaoService.buscarMaxFilaDoEquipamento(equipamento.getId());
        int proximaFila = (ultimaFila != null ? ultimaFila : 0) + 1;

        programacao.setFila(proximaFila);
        
       
        programacao.setStatus(StatusProgramacao.CRIADO);

        lote.consumirQuantidade(programacaoRequestDTO.quantidadeConsumida());


        lote.adicionarProgramacao(programacao);

        programacaoService.salvarProgramacao(programacao);
       


        return     programacaoMapper.toDTODetalhe(programacao,lote,equipamento);


    }

    @Transactional
    public void desabastecerLote(Long idLote){
        Lote lote = loteService.buscarLotePorId(idLote);
        Programacao programacao  =   loteService.buscarUltimaProgramacaoPorLote(idLote);

        if (lote.getStatus() != StatusLote.ABASTECIDO) {
            throw new AbastecimentoLoteException("Lote " + lote.getNome() + " não está abastecido");
        }
        lote.setStatus(StatusLote.DESABASTECIDO);

        programacaoService.deletarProgramacaoPorId(programacao.getId());
        lote.removerProgramacao(programacao);

        loteService.salvarLote(lote);

    }

    @Transactional
    public ProgramacaoResumoResponseDTO alterarStatus(Long programacaoId, ProgramacaoRequestDTO dto, StatusProgramacao novoStatus) {

        MudancaStatusStrategy estrategia = estrategias.get(novoStatus);
        if (estrategia == null) {
            throw new IllegalArgumentException("Ação não configurada para o status: " + novoStatus);
        }

        Programacao programacao = programacaoService.buscarProgramacaoPorId(programacaoId);

        if (!estrategia.permiteTransicao(programacao.getStatus())) {
            throw new NotProgramacaoValidException("Transição inválida de " + programacao.getStatus() + " para " + novoStatus);
        }


        Lote lote = estrategia.processarLote(loteService, dto);
        Equipamento equipamento = equipamentoService.buscarEquipamentoPorId(dto.equipamentoId());

        estrategia.finalizarProgramacao(programacao, programacaoService);
        programacaoService.salvarProgramacao(programacao);
        estrategia.registrarRastreabilidade(lote, equipamento, rastreabilidadeService);

        

        return programacaoMapper.toDTOResumo(programacao, lote, equipamento);
    }

    @Transactional
    public ProgramacaoResumoResponseDTO retirarDeQualidadeProgramacao(Long id , ProgramacaoRequestDTO programacaoRequestDTO) {

        loteService.retirarLoteEmQualidade(programacaoRequestDTO.loteId());
        return alterarStatus(id,programacaoRequestDTO,StatusProgramacao.PROGRAMADO);

    }

    @Transactional
    public void deletarEquipamentoComValidacao(Long equipamentoId) {


        List<Programacao> programasAbastecidos = programacaoService
                .buscarProgramacoesPorEquipamentoAndStatus(equipamentoId, StatusProgramacao.ABASTECIDO);


        if (!programasAbastecidos.isEmpty()) {
            throw new LoteAbastecidoException("Tem programa ainda como abastecido. Portanto não é possível deletar o Equipamento.");
        }


        equipamentoService.deletarEquipamento(equipamentoId);
    }

    
    public List<ProgramacaoOrdemProducaoDTO> buscarProgramacoesPorEquipamentoAndStatus(Long equipamentoId, StatusProgramacao status) {
        
        List<ProgramacaoOrdemProducaoDTO> programacao = programacaoService.buscarProgramacoesPorEquipamentoIdEStatus(equipamentoId, status);
        Equipamento equipamento = equipamentoService.buscarEquipamentoPorId(equipamentoId);
    

        
        if(equipamento.getStatusEquipamento() == StatusEquipamento.PARADO){
            throw new NotFoundEquipamentoException("Nenhum equipamento encontrado com esse id" + equipamentoId);
        }
        

        return programacao;

    }

}
