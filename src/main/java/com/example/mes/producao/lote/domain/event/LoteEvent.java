package com.example.mes.producao.lote.domain.event;

import com.example.mes.producao.programacao.domain.Programacao;
import com.example.mes.producao.programacao.domain.StatusProgramacao;

public record LoteEvent(StatusProgramacao statusProgramacao, Programacao programacao  ) {

}
