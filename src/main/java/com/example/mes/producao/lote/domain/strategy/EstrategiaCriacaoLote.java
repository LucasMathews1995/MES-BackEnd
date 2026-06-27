package com.example.mes.producao.lote.domain.strategy;

import java.util.List;

import com.example.mes.producao.equipamento.model.Equipamento;
import com.example.mes.producao.lote.domain.Lote;
import com.example.mes.producao.ordemproducao.domain.OrdemProducao;

public interface EstrategiaCriacaoLote {

boolean deveFracionar(Equipamento eq, int quantidade);
List<Lote> executar(OrdemProducao op, Lote lotePai, int quantidade,int capacidade);
}
