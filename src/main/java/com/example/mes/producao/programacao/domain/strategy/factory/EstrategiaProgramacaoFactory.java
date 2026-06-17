package com.example.mes.producao.programacao.domain.strategy.factory;

import java.util.List;

import org.springframework.stereotype.Component;

import com.example.mes.producao.programacao.domain.StatusProgramacao;
import com.example.mes.producao.programacao.domain.exceptions.NotFoundProgramacaoException;
import com.example.mes.producao.programacao.domain.strategy.EstrategiaProgramacao;

@Component
public class EstrategiaProgramacaoFactory {

    
    private final List<EstrategiaProgramacao> estrategias;

   
    public EstrategiaProgramacaoFactory(List<EstrategiaProgramacao> estrategias) {
        this.estrategias = estrategias;
    }

    public EstrategiaProgramacao obterEstrategia(StatusProgramacao statusProgramacao) {
        return estrategias.stream()
                .filter(est -> est.getStatusAlvo() == statusProgramacao)
                .findFirst()
                .orElseThrow(() -> new NotFoundProgramacaoException("Estratégia não disponível para o status: " + statusProgramacao));
    }



}
