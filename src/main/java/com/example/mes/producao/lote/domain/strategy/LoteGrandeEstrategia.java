package com.example.mes.producao.lote.domain.strategy;

import java.util.List;

import org.springframework.stereotype.Component;

import com.example.mes.producao.equipamento.model.Equipamento;
import com.example.mes.producao.lote.domain.Lote;
import com.example.mes.producao.ordemproducao.domain.OrdemProducao;

@Component
public class LoteGrandeEstrategia implements EstrategiaCriacaoLote {

    @Override
    public boolean deveFracionar(Equipamento eq, Long quantidade) {
        return eq.getCapacidade() == quantidade;
    }

    @Override
    public List<Lote> executar(OrdemProducao op, Lote lote, Long quantidade, Long capacidade) {
        lote.consumirQuantidade(quantidade);
        
        return List.of(lote);
    }

}
