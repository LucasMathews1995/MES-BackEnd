package com.example.mes.producao.programacao.domain.strategy;

import org.springframework.stereotype.Component;

import com.example.mes.producao.programacao.domain.Programacao;
import com.example.mes.producao.programacao.domain.StatusProgramacao;
import com.example.mes.producao.programacao.infraestructure.persistence.ProgramacaoRepository;



@Component
public class EstrategiaQualidade implements EstrategiaProgramacao {

   private final ProgramacaoRepository programacaoRepository;
   public EstrategiaQualidade(ProgramacaoRepository programacaoRepository) {
        this.programacaoRepository = programacaoRepository;
    }

    @Override
    public StatusProgramacao getStatusAlvo() {
        return StatusProgramacao.QUALIDADE;
    }

    @Override
    public void processar(Programacao programacao) {
        if (programacao.getStatus() != StatusProgramacao.QUALIDADE) {
            programacao.colocarEmQualidade();
            programacaoRepository.save(programacao);
        }else if(programacao.getStatus() == StatusProgramacao.QUALIDADE){
            programacao.retirarDaQualidade();
            programacaoRepository.save(programacao);
        }
    }



}
