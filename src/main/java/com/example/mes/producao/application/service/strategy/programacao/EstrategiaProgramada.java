package com.example.mes.producao.application.service.strategy.programacao;

import com.example.mes.producao.application.dto.ProgramacaoRequestDTO;
import com.example.mes.producao.application.service.LoteService;
import com.example.mes.producao.application.service.ProgramacaoService;
import com.example.mes.producao.application.service.RastreabilidadeService;
import com.example.mes.producao.domain.Equipamento;
import com.example.mes.producao.domain.Lote;
import com.example.mes.producao.domain.Programacao;
import com.example.mes.producao.domain.StatusProgramacao;
import com.example.mes.producao.domain.StatusRastreabilidade;

import java.time.LocalDateTime;

import org.springframework.stereotype.Component;

@Component
public class EstrategiaProgramada implements MudancaStatusStrategy {
    @Override
    public StatusProgramacao getStatusAlvo() {
        return StatusProgramacao.PROGRAMADO;
    }

    @Override
    public Lote processarLote(LoteService loteService, Long LoteId) {
        return loteService.programarLote(LoteId);
    }

    @Override
    public void finalizarProgramacao(Programacao programacao) {
        programacao.setStatus(StatusProgramacao.PROGRAMADO);
    }

    @Override
    public boolean permiteTransicao(StatusProgramacao statusAtual) {
        return statusAtual == StatusProgramacao.CRIADO || statusAtual == StatusProgramacao.QUALIDADE;
    }

    @Override
    public void registrarRastreabilidade(Lote lote, Equipamento equipamento,
            RastreabilidadeService rastreabilidadeService) {
        String evento = "O lote " + lote.getNome() + "foi programado no equipamento " + equipamento.getNome() + " em "
                + LocalDateTime.now();
                
        rastreabilidadeService.registrarEventoRastreabilidade(lote, equipamento, StatusRastreabilidade.PROGRAMADO,
                evento);
    }

}
