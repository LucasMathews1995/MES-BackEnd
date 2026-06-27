package com.example.mes.producao.lote.domain.strategy;


import org.springframework.stereotype.Component;

import com.example.mes.producao.lote.domain.Lote;
import com.example.mes.producao.lote.domain.StatusLote;
import com.example.mes.producao.programacao.domain.Programacao;

@Component
public class EstrategiaLoteConsumido implements EstrategiaLote {

    @Override
    public StatusLote getStatusAlvo() {
        return StatusLote.CONSUMIDO;
    }

    @Override
    public void processar(Programacao programacao) {

        Lote lote = programacao.getLoteConsumido();
      
           lote.consumirLote();
        } 
           
        

    

}
