package com.example.mes.producao.application.service;


import com.example.mes.producao.api.exception.ProgramacaoNotFoundException;
import com.example.mes.producao.domain.Equipamento;
import com.example.mes.producao.domain.Programacao;
import com.example.mes.producao.domain.StatusProgramacao;
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



    @Transactional
    public void salvarProgramacao(Programacao programacao){

        programacaoRepository.save(programacao);
    }

    public Programacao buscarProgramacaoPorId(Long id){
       return  programacaoRepository.findById(id).orElseThrow(()-> new ProgramacaoNotFoundException("Nenhuma programacao encontrada"));
    }


    public List<Programacao> buscarProgramacoesPorEquipamentoAndStatus(Long equipamentoId, StatusProgramacao status){
        return programacaoRepository.findAllByEquipamentoIdAndStatus(equipamentoId,status).orElseThrow(()-> new ProgramacaoNotFoundException("Não há progrmacão com essse id de equipamento  : " + equipamentoId));
    }

    public List<Programacao>  buscarProgramacoesPorEquipamentoAteProduzido(Long equipamentoId){

        List<StatusProgramacao> statusIgnorados = Arrays.asList(
                StatusProgramacao.PRODUZIDO,
                StatusProgramacao.APROVADO,
                StatusProgramacao.QUALIDADE
        );
        return programacaoRepository.findByEquipamentoIdAndStatusNotIn(equipamentoId, statusIgnorados);
    }



    public List<Programacao> buscarTodasProgramacoes(){
        List<Programacao> programacoes =  programacaoRepository.findAll();
        if(programacoes.isEmpty()){
            throw new ProgramacaoNotFoundException("Nenhuma programacao encontrada");
        }
        return programacoes;
    }
     @Transactional
    public void deletarProgramacaoPorId(Long id){
        programacaoRepository.deleteById(id);
    }
    public boolean existirProgamaPorLoteId(Long loteId){
        return programacaoRepository.existsByLoteId(loteId);
    }








    public Integer buscarMaxFilaDoEquipamento(Long equipamentoId){return programacaoRepository.findMaxFilaByEquipamentoId(equipamentoId);}


}
