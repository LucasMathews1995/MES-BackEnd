package com.example.mes.producao.application.service.strategy.programacao;

import com.example.mes.producao.domain.Equipamento;
import com.example.mes.producao.domain.Lote;
import com.example.mes.producao.domain.Programacao;
import com.example.mes.producao.domain.StatusProgramacao;

public interface MudancaStatusStrategy {

    StatusProgramacao getStatusAlvo();

 
    void processar(Programacao programacao, Lote lote, Equipamento equipamento);


}
