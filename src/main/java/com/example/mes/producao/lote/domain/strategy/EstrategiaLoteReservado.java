package com.example.mes.producao.lote.domain.strategy;


import org.springframework.stereotype.Component;
import com.example.mes.producao.lote.domain.Lote;
import com.example.mes.producao.lote.domain.StatusLote;
import com.example.mes.producao.programacao.domain.Programacao;





@Component
public class EstrategiaLoteReservado implements EstrategiaLote {

    @Override
    public StatusLote getStatusAlvo() {
        return StatusLote.RESERVADO;
    }

    @Override
    public void processar(Programacao programacao)  {
        
        programacao.getLoteConsumido().reservarLote(programacao.getQuantidadeConsumida());
 
    }

}
