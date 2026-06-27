package com.example.mes.producao.programacao.domain.strategy;


import org.springframework.stereotype.Component;


import com.example.mes.producao.programacao.domain.Programacao;
import com.example.mes.producao.programacao.domain.StatusProgramacao;
import com.example.mes.producao.programacao.domain.exceptions.ProgramacaoNotValidException;

@Component
public class EstrategiaProgramada implements EstrategiaProgramacao {

    @Override
    public StatusProgramacao getStatusAlvo() {
     return StatusProgramacao.PROGRAMADA;
    }

    @Override
    public void processar(Programacao programacao,Integer ultimaFila) {
       
     

        if(!programacao.getStatus().podeMudarPara(StatusProgramacao.PROGRAMADA)) {
            throw new ProgramacaoNotValidException("Apenas programações no status CRIADO podem ser programadas.");

        }
     

        programacao.programarLote();
        programacao.setFila(ultimaFila);
       

    }



    
}