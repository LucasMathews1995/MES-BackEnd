package com.example.mes.producao.programacao.domain.strategy.qualidade;

import org.springframework.stereotype.Component;

import com.example.mes.producao.programacao.domain.Programacao;
import com.example.mes.producao.programacao.domain.StatusProgramacao;

@Component
public class EstrategiaRetirarQualidade  implements EstrategiaQualidade{

    @Override
    public StatusProgramacao getStatusAlvo() {
      return  StatusProgramacao.DESABASTECIDO;
    }

    @Override
    public void processar(Programacao programacao) {
        programacao.retirarDaQualidade();
        

    }

}
