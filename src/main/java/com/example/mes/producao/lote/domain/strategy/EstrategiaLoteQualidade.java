package com.example.mes.producao.lote.domain.strategy;

import org.springframework.stereotype.Component;


import com.example.mes.producao.lote.domain.StatusLote;
import com.example.mes.producao.programacao.domain.Programacao;


@Component
public class EstrategiaLoteQualidade implements EstrategiaLote {

    @Override
    public StatusLote getStatusAlvo() {
        return StatusLote.QUALIDADE;
    }

    @Override
    public void processar(Programacao programacao) {
        programacao.getLoteConsumido().rejeitarLote(programacao.getQuantidadeConsumida());

    }

}
