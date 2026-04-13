package com.example.mes.producao.application.service.strategy.programacao;

import com.example.mes.producao.application.dto.ProgramacaoRequestDTO;
import com.example.mes.producao.application.service.LoteService;
import com.example.mes.producao.application.service.ProgramacaoService;
import com.example.mes.producao.domain.Lote;
import com.example.mes.producao.domain.Programacao;
import com.example.mes.producao.domain.StatusProgramacao;
import org.springframework.stereotype.Component;

@Component
public class EstrategiaProgramada implements MudancaStatusStrategy{
    @Override
    public StatusProgramacao getStatusAlvo() {
        return StatusProgramacao.PROGRAMADO;
    }

    @Override
    public Lote processarLote(LoteService loteService, ProgramacaoRequestDTO dto) {
        return loteService.programarLote(dto.loteId());
    }

    @Override
    public void finalizarProgramacao(Programacao programacao, ProgramacaoService programacaoService) {
        programacao.setStatus(StatusProgramacao.PROGRAMADO);
    }

    @Override
    public boolean permiteTransicao(StatusProgramacao statusAtual) {
        return statusAtual.equals(StatusProgramacao.CRIADO);
    }
}
