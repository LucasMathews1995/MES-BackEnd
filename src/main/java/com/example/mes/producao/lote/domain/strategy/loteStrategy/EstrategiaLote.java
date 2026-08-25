package com.example.mes.producao.lote.domain.strategy.loteStrategy;


import org.springframework.stereotype.Component;
import com.example.mes.producao.lote.domain.StatusLote;
import com.example.mes.producao.programacao.domain.Programacao;



@Component
public interface EstrategiaLote  {
    StatusLote getStatusAlvo();
    void processar(Programacao programacao);  
   

}
