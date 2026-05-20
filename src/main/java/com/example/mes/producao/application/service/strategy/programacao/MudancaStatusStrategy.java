package com.example.mes.producao.application.service.strategy.programacao;

import com.example.mes.producao.application.dto.ProgramacaoRequestDTO;
import com.example.mes.producao.application.service.LoteService;
import com.example.mes.producao.application.service.ProgramacaoService;
import com.example.mes.producao.application.service.RastreabilidadeService;
import com.example.mes.producao.domain.Equipamento;
import com.example.mes.producao.domain.Lote;
import com.example.mes.producao.domain.Programacao;
import com.example.mes.producao.domain.StatusProgramacao;

public interface MudancaStatusStrategy {

    StatusProgramacao getStatusAlvo();

    Lote processarLote(LoteService loteService, Long dto);

    void finalizarProgramacao(Programacao programacao);

    boolean permiteTransicao(StatusProgramacao statusAtual);
    
    void registrarRastreabilidade(Lote lote, Equipamento equipamento, RastreabilidadeService rastreabilidadeService);
}
