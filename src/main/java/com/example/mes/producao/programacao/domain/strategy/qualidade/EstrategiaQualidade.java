package com.example.mes.producao.programacao.domain.strategy.qualidade;

import com.example.mes.producao.programacao.domain.Programacao;
import com.example.mes.producao.programacao.domain.StatusProgramacao;

public interface EstrategiaQualidade {

    StatusProgramacao getStatusAlvo();
 
//colocar metodo para tipo de RDQ que foi colocado e retirado

    void processar(Programacao programacao);

}
