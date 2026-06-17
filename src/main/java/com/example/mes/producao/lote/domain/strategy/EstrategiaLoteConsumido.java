package com.example.mes.producao.lote.domain.strategy;


import com.example.mes.producao.lote.domain.StatusLote;
import com.example.mes.producao.programacao.domain.Programacao;

public class EstrategiaLoteConsumido implements EstrategiaLote {

    @Override
    public StatusLote getStatusAlvo() {
        return StatusLote.CONSUMIDO;
    }

    @Override
    public void processar(Programacao programacao) {
        if (programacao.getLoteConsumido().getQuantidadeDisponivel() == 0) {
            programacao.getLoteConsumido().consumirLote(programacao.getQuantidadeConsumida());
        } else {
            programacao.getLoteConsumido().abastecerLote();
        }

    }

}
