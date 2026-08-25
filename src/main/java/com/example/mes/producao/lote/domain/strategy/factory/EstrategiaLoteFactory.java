package com.example.mes.producao.lote.domain.strategy.factory;

import java.util.List;

import org.springframework.stereotype.Component;

import com.example.mes.producao.lote.domain.StatusLote;
import com.example.mes.producao.lote.domain.exceptions.EstrategiaNotFoundException;
import com.example.mes.producao.lote.domain.strategy.loteStrategy.EstrategiaLote;

@Component
public class EstrategiaLoteFactory {
    
 

    private final List<EstrategiaLote> estrategias;

   
    public EstrategiaLoteFactory(List<EstrategiaLote> estrategias) {
        this.estrategias = estrategias;
    }

    public EstrategiaLote obterEstrategia(StatusLote statusLote) {
        return estrategias.stream()
                .filter(est -> est.getStatusAlvo() == statusLote)
                .findFirst()
                .orElseThrow(() -> new EstrategiaNotFoundException("Estratégia não disponível para o status: " + statusLote));
    }
}

