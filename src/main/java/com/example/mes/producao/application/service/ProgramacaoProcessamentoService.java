package com.example.mes.producao.application.service;

import com.example.mes.producao.api.exception.NotProgramacaoValidException;
import com.example.mes.producao.application.dto.ProgramacaoRequestDTO;
import com.example.mes.producao.application.mapper.ProgramacaoMapper;
import com.example.mes.producao.domain.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ProgramacaoProcessamentoService {

    private final ProgramacaoService programacaoService;
    private final EquipamentoService equipamentoService;
    private final LoteProcessamentoService loteService;
    private final ProgramacaoMapper programacaoMapper;
    private final LoteDataService loteDataService;

    @Transactional
    public Programacao programarProgramacao(ProgramacaoRequestDTO dto){

        Equipamento equipamento = equipamentoService.buscarEquipamentoPorId(dto.equipamentoId());

        Lote lote = loteDataService.buscarLotePorId(dto.loteId());

        if(programacaoService.existirProgamaPorLoteId(lote.getId()) || equipamento.getStatusEquipamento().equals(StatusEquipamento.PARADO)){
            throw new NotProgramacaoValidException("Esse lote " + lote.getNome()+ "já possui programa");

        }

        Programacao programacao = programacaoMapper.toEntity(lote,equipamento,dto);

        Integer ultimaFila = programacaoService.buscarMaxFilaDoEquipamento(equipamento.getId());
        int proximaFila = (ultimaFila != null ? ultimaFila : 0) + 1;

        programacao.setFila(proximaFila);
        lote.consumirQuantidade(dto.quantidadeConsumida());


        lote.adicionarProgramacao(programacao);

        return  programacao;
    }

    @Transactional
    public Programacao abastecerProgramacao(Long programacaoId, ProgramacaoRequestDTO dto){

        loteService.abastecerLote(dto.loteId());
        Programacao programacao = programacaoService.buscarProgramacaoPorId(programacaoId);

       if(validarTransacao(programacao.getStatus(),StatusProgramacao.ABASTECIDO)){
           throw new NotProgramacaoValidException("não é possível abastecer o lote pois o status da programação é : " + programacao.getStatus());
       }
       programacao.setStatus(StatusProgramacao.ABASTECIDO);

       return programacao;
    }
    @Transactional
    public Programacao produzirPrograma(Long programacaoId, ProgramacaoRequestDTO dto){

        loteService.produzirLote(dto.loteId());
        Programacao programacao = programacaoService.buscarProgramacaoPorId(programacaoId);

        if(validarTransacao(programacao.getStatus(),StatusProgramacao.PRODUZIDO)){
            throw new NotProgramacaoValidException("não é possível abastecer o lote pois o status da programação é : " + programacao.getStatus());
        }

        programacao.setStatus(StatusProgramacao.PRODUZIDO);

        return programacao;
    }
    @Transactional
    public Programacao aprovarPrograma(Long programacaoId, ProgramacaoRequestDTO dto){

        loteService.aprovarLote(dto.loteId());
        Programacao programacao = programacaoService.buscarProgramacaoPorId(programacaoId);

        if(validarTransacao(programacao.getStatus(),StatusProgramacao.APROVADO)){
            throw new NotProgramacaoValidException("não é possível abastecer o lote pois o status da programação é : " + programacao.getStatus());
        }

        programacao.setStatus(StatusProgramacao.APROVADO);


        return programacao;
    }
    @Transactional
    public void colocarEmQualidadeProgramacao(Long programacaoId, ProgramacaoRequestDTO dto){

        loteService.colocarLoteEmQualidade(dto.loteId(),dto.quantidadeConsumida());

        Programacao programacao = programacaoService.buscarProgramacaoPorId(programacaoId);

        if(validarTransacao(programacao.getStatus(),StatusProgramacao.QUALIDADE)){
            throw new NotProgramacaoValidException("não é possível abastecer o lote pois o status da programação é : " + programacao.getStatus());
        }

        programacaoService.deletarProgramacaoPorId(programacaoId);

    }

    @Transactional
    public Programacao retirarDeQualidadeProgramacao( ProgramacaoRequestDTO dto) {

        loteService.retirarLoteEmQualidade(dto.loteId());


        return programarProgramacao(dto);

    }



    @Transactional
    public void resequenciarPrograma (Long id, Long idTtroca){
        Programacao programacao = programacaoService.buscarProgramacaoPorId(id);
        Programacao programacaoTroca = programacaoService.buscarProgramacaoPorId(idTtroca);

        int numeroSequencia = programacao.getFila();

        programacao.setFila(programacaoTroca.getFila());
        programacaoTroca.setFila(numeroSequencia);



    }

    






    private boolean validarTransacao(StatusProgramacao status, StatusProgramacao statusNovo){

        return switch(status){
               case CRIADA -> statusNovo == StatusProgramacao.PROGRAMADO || statusNovo == StatusProgramacao.QUALIDADE;
               case PROGRAMADO -> statusNovo == StatusProgramacao.ABASTECIDO || statusNovo == StatusProgramacao.QUALIDADE;
               case ABASTECIDO -> statusNovo == StatusProgramacao.PRODUZIDO || statusNovo == StatusProgramacao.QUALIDADE;
               case PRODUZIDO -> statusNovo == StatusProgramacao.APROVADO || statusNovo == StatusProgramacao.QUALIDADE;
               case APROVADO, QUALIDADE -> false;
        };
    }



}
