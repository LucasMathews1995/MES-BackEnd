package com.example.mes.producao.lote.domain.strategy;

import com.example.mes.producao.lote.domain.StatusLote;
import com.example.mes.producao.programacao.domain.Programacao;

public class EstrategiaLoteRetirarQualidade implements EstrategiaLote {

    @Override
    public StatusLote getStatusAlvo() {
        return StatusLote.DECISAO;
    }

    @Override
    public void processar(Programacao programacao) {

        
        programacao.retirarDaQualidade();
    }

}
