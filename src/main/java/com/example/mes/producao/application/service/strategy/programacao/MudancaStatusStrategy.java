package com.example.mes.producao.application.service.strategy.programacao;

import com.example.mes.producao.application.dto.ProgramacaoRequestDTO;
import com.example.mes.producao.application.service.LoteService;
import com.example.mes.producao.application.service.ProgramacaoService;
import com.example.mes.producao.domain.Lote;
import com.example.mes.producao.domain.Programacao;
import com.example.mes.producao.domain.StatusProgramacao;

public interface MudancaStatusStrategy {

    StatusProgramacao getStatusAlvo();

    Lote processarLote(LoteService loteService, ProgramacaoRequestDTO dto);

    void finalizarProgramacao(Programacao programacao, ProgramacaoService programacaoService);

    boolean permiteTransicao(StatusProgramacao statusAtual);
}
