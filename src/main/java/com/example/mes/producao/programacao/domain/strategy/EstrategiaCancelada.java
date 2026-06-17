package com.example.mes.producao.programacao.domain.strategy;

import org.springframework.stereotype.Component;

import com.example.mes.producao.lote.domain.Lote;
import com.example.mes.producao.programacao.domain.Programacao;
import com.example.mes.producao.programacao.domain.StatusProgramacao;


@Component
public class EstrategiaCancelada implements EstrategiaProgramacao{

   

    @Override
    public StatusProgramacao getStatusAlvo() {
        return StatusProgramacao.CANCELADA;
    }

    @Override
    public void processar(Programacao programacao,Integer ultimaFila) {
       Lote lote = programacao.getLoteConsumido();
            lote.abastecerLote();
       

        programacao.setStatus(StatusProgramacao.CANCELADA);
        programacao.setFila(null);




    }


  
    
}
