package com.example.mes.producao.programacao.domain.strategy;


import com.example.mes.producao.programacao.domain.Programacao;
import com.example.mes.producao.programacao.domain.StatusProgramacao;

public interface EstrategiaProgramacao {

    StatusProgramacao getStatusAlvo();

    void processar(Programacao programacao,Integer ultimaFila);


}
