package com.example.mes.producao.application.service;


import com.example.mes.producao.api.exception.NotFoundEquipamentoException;
import com.example.mes.producao.api.exception.ProgramacaoNotFoundException;
import com.example.mes.producao.application.dto.ProgramacaoOrdemProducaoDTO;
import com.example.mes.producao.application.dto.ProgramacaoResponseDTO;
import com.example.mes.producao.application.mapper.ProgramacaoMapper;
import com.example.mes.producao.domain.Programacao;
import com.example.mes.producao.domain.StatusProgramacao;
import com.example.mes.producao.infraestructure.EquipamentoRepository;
import com.example.mes.producao.infraestructure.ProgramacaoRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

@Service
@AllArgsConstructor
public class ProgramacaoService {

    private final ProgramacaoRepository programacaoRepository;
    private final EquipamentoRepository equipamentoRepository;
    private final ProgramacaoMapper programacaoMapper;



    @Transactional
    public void salvarProgramacao(Programacao programacao){

        programacaoRepository.save(programacao);
    }

    public Programacao buscarProgramacaoPorId(Long id){
       return  programacaoRepository.findById(id).orElseThrow(()-> new ProgramacaoNotFoundException("Nenhuma programacao encontrada"));
    }
    public List<ProgramacaoOrdemProducaoDTO> buscarProgramacaoCriado(Long id){

       List<ProgramacaoOrdemProducaoDTO> programacoesCriadas = programacaoRepository.findProgramacoesCriadas(id);
       if(programacoesCriadas.isEmpty()){
           throw new  ProgramacaoNotFoundException("Nenhuma programacao encontrada");
       }
       return programacoesCriadas;
    }



    public List<Programacao> buscarProgramacoesPorEquipamentoAndStatus(Long equipamentoId, StatusProgramacao status){
     List<Programacao> programacao =   programacaoRepository.findAllByEquipamentoIdAndStatus(equipamentoId,status);

        if(programacao.isEmpty()){
            throw new ProgramacaoNotFoundException("Nenhuma programacao encontrada com esse equipamento" +  equipamentoId);
        }

        return programacao;


    }


    public List<ProgramacaoResponseDTO> buscarProgramacoesPorEquipamentoAteProduzido(Long equipamentoId) {

        List<StatusProgramacao> statusIgnorados = Arrays.asList(
                StatusProgramacao.PRODUZIDO,
                StatusProgramacao.APROVADO,
                StatusProgramacao.QUALIDADE
        );

        if (!equipamentoRepository.existsById(equipamentoId)) {
            throw new NotFoundEquipamentoException("Nenhuma equipamento encontrada");
        }

        List<Programacao> programacoes = programacaoRepository.findByEquipamentoIdAndStatusNotIn(equipamentoId, statusIgnorados);

        return programacoes.stream().map(it -> programacaoMapper.toDTODetalhe(it, it.getLote(), it.getEquipamento())).toList();

    }


    public List<ProgramacaoOrdemProducaoDTO> buscarProgrmacaoDoEquipamento(Long equipamentoId){

        List<ProgramacaoOrdemProducaoDTO> programacoes =    programacaoRepository.findProgramacaoByEquipamento(equipamentoId);

        if (programacoes.isEmpty()) {
           return null;
        }

        return programacoes;
    }






    public List<ProgramacaoResponseDTO> buscarTodasProgramacoes(){
        List<Programacao> programacoes =  programacaoRepository.findAll();

        if(programacoes.isEmpty()){
            throw new ProgramacaoNotFoundException("Nenhuma programacao encontrada");
        }

        return programacoes.stream().map(it-> programacaoMapper.toDTODetalhe(it,it.getLote(),it.getEquipamento())).toList();
    }



     @Transactional
    public void deletarProgramacaoPorId(Long id){
        programacaoRepository.deleteById(id);
    }



    public boolean existirProgamaPorLoteId(Long loteId){
        return programacaoRepository.existsByLoteId(loteId);
    }



    public Integer buscarMaxFilaDoEquipamento(Long equipamentoId){return programacaoRepository.findMaxFilaByEquipamentoId(equipamentoId);}


    @Transactional
    public void resequenciarPrograma(Long id, Long idTtroca) {
        Programacao programacao = buscarProgramacaoPorId(id);
        Programacao programacaoTroca = buscarProgramacaoPorId(idTtroca);

        int numeroSequencia = programacao.getFila();

        programacao.setFila(programacaoTroca.getFila());
        programacaoTroca.setFila(numeroSequencia);


    }

}
