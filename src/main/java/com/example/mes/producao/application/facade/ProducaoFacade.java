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

    public ProducaoFacade(List<MudancaStatusStrategy> listaEstrategias, LoteService loteService,
            OrdemProducaoService ordemService,
            EquipamentoService equipamentoService, ProgramacaoService programacaoService,
            ProgramacaoMapper programacaoMapper, RastreabilidadeService rastreabilidadeService) {

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
    public OrdemProducao vincularOrdemProducaoAoLote(Long idProd, Long idLote) {

        Lote lote = loteService.buscarLotePorId(idLote);

        return ordemService.vincularLote(idProd, lote);
    }

    @Transactional
    public ProgramacaoResponseDTO criarProgramaDoLote(ProgramacaoRequestDTO programacaoRequestDTO) {

        Equipamento equipamento = equipamentoService.buscarEquipamentoPorId(programacaoRequestDTO.equipamentoId());

        Lote lote = loteService.buscarLotePorId(programacaoRequestDTO.loteId());

        Programacao programacao = programacaoMapper.toEntity(lote, equipamento, programacaoRequestDTO);

        programacaoService.criarPrograma(programacao,
                lote,
                equipamento,
                programacaoRequestDTO.quantidadeConsumida());
        rastreabilidadeService.criarRastreabilidadeParaProgramacao(programacao, lote, equipamento);

        return programacaoMapper.toDTODetalhe(programacao, lote, equipamento);

    }

    @Transactional
    public void desabastecerLote(Long idLote) {
        Lote lote = loteService.buscarLotePorId(idLote);
        Programacao programacao = loteService.buscarUltimaProgramacaoPorLote(idLote);

        if (lote.getStatus() != StatusLote.ABASTECIDO) {
            throw new AbastecimentoLoteException("Lote " + lote.getNome() + " não está abastecido");
        }

            lote.desabastecerLote(programacao);
    

        rastreabilidadeService.registrarRastreabilidadeLote(lote, programacao.getEquipamento(), "Lote desabastecido: " + lote.getNome());
       
        

        loteService.salvarLote(lote);

    }

    @Transactional
    public ProgramacaoResumoResponseDTO alterarStatus(Long programacaoId,
            StatusProgramacao novoStatus) {

        MudancaStatusStrategy estrategia = estrategias.get(novoStatus);
        if (estrategia == null) {
            throw new IllegalArgumentException("Ação não configurada para o status: " + novoStatus);
        }

        Programacao programacao = programacaoService.buscarProgramacaoPorId(programacaoId);

        if (!estrategia.permiteTransicao(programacao.getStatus())) {
            throw new NotProgramacaoValidException(
                    "Transição inválida de " + programacao.getStatus() + " para " + novoStatus);
        }

        Lote lote = estrategia.processarLote(loteService, programacao.getLote().getId());
        Equipamento equipamento = equipamentoService.buscarEquipamentoPorId(programacao.getEquipamento().getId());

        estrategia.finalizarProgramacao(programacao);

        estrategia.registrarRastreabilidade(lote, equipamento, rastreabilidadeService);

        return programacaoMapper.toDTOResumo(programacao, lote, equipamento);
    }

    @Transactional
    public ProgramacaoResumoResponseDTO retirarDeQualidadeProgramacao(Long id, RetirarQualidadeDTO requestDTO) {

        OrdemProducao ordem = ordemService.buscarPorId(requestDTO.OpId());
        Equipamento equipamento = equipamentoService.buscarEquipamentoPorId(requestDTO.equipamentoId());

        Programacao programacao = programacaoService.retirarQualidade(equipamento, ordem, id);

        return programacaoMapper.toDTOResumo(programacao, programacao.getLote(), equipamento);

    }

    @Transactional
    public void desativarEquipamento(Long equipamentoId) {

        Equipamento equipamento = equipamentoService.buscarEquipamentoPorId(equipamentoId);

   
       programacaoService
                .excluirProgramasDoEquipamentoParados(equipamentoId);

        rastreabilidadeService.registrarRastreabilidadeEquipamento(equipamento, "Equipamento desativado: " + equipamento.getNome());

        equipamentoService.desativarEquipamento(equipamentoId);
    }

    public List<ProgramacaoOrdemProducaoDTO> buscarProgramacoesPorEquipamentoAndStatus(Long equipamentoId,
            StatusProgramacao status) {

        List<ProgramacaoOrdemProducaoDTO> programacao = programacaoService
                .buscarProgramacoesPorEquipamentoIdEStatus(equipamentoId, status);
        Equipamento equipamento = equipamentoService.buscarEquipamentoPorId(equipamentoId);

        if (equipamento.getStatusEquipamento() == StatusEquipamento.PARADO) {
            throw new NotFoundEquipamentoException("Nenhum equipamento encontrado com esse id" + equipamentoId);
        }

        return programacao;

    }

    public void ativarEquipamento(Long id) {
        Equipamento equipamento = equipamentoService.buscarEquipamentoPorId(id);

        if (equipamento.getStatusEquipamento() != StatusEquipamento.PARADO) {
            throw new NotFoundEquipamentoException("Nenhum equipamento encontrado com esse id" + id);
        }

        rastreabilidadeService.registrarRastreabilidadeEquipamento(equipamento, "Equipamento ativado: " + equipamento.getNome());

        equipamentoService.ativarEquipamento(id);
    }

}
