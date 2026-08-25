package com.example.mes.producao.programacao.domain.strategy.qualidade.factory;

import java.util.List;

import org.springframework.stereotype.Component;

import com.example.mes.producao.programacao.domain.StatusProgramacao;
import com.example.mes.producao.programacao.domain.exceptions.NotFoundProgramacaoException;
import com.example.mes.producao.programacao.domain.strategy.qualidade.EstrategiaQualidade;


@Component
public class EstrategiaQualidadeFactory {

    private final List<EstrategiaQualidade> estrategias;

   
    public EstrategiaQualidadeFactory(List<EstrategiaQualidade> estrategias) {
        this.estrategias = estrategias;
    }

    public EstrategiaQualidade obterEstrategia(StatusProgramacao statusProgramacao) {
        return estrategias.stream()
                .filter(est -> est.getStatusAlvo() == statusProgramacao)
                .findFirst()
                .orElseThrow(() -> new NotFoundProgramacaoException("Estratégia não disponível para o status: " + statusProgramacao));
    }

}
