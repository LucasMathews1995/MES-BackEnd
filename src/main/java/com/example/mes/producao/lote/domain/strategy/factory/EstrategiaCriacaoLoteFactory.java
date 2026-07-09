package com.example.mes.producao.lote.domain.strategy.factory;

import java.util.List;

import org.springframework.stereotype.Component;

import com.example.mes.producao.equipamento.model.Equipamento;
import com.example.mes.producao.lote.domain.exceptions.EstrategiaNotFoundException;
import com.example.mes.producao.lote.domain.strategy.EstrategiaCriacaoLote;
@Component
public class EstrategiaCriacaoLoteFactory {



    private List<EstrategiaCriacaoLote> estrategia;
    
    public EstrategiaCriacaoLoteFactory(List<EstrategiaCriacaoLote> estrategia){
        this.estrategia =estrategia;
    }

    public EstrategiaCriacaoLote obEstrategiaCriacaoLote(Equipamento equipamento,Long quantidade){
       return estrategia.stream()
        .filter(est-> est.deveFracionar(equipamento,quantidade ))
        .findFirst()
        .orElseThrow(() -> new EstrategiaNotFoundException("Nenhuma estratégia encontrada para os parâmetros informados"));
    }

}
