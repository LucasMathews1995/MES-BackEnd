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
public class EstrategiaAprovado implements MudancaStatusStrategy {

    @Override
    public StatusProgramacao getStatusAlvo() {
        return StatusProgramacao.APROVADO;
    }

    @Override
    public Lote processarLote(LoteService loteService, Long LoteId) {

        return loteService.aprovarLote(LoteId);
    }

    @Override
    public void finalizarProgramacao(Programacao programacao ) {
        programacao.setStatus(StatusProgramacao.APROVADO);
        programacao.getLote().setOrdemProducao(null);
       
    }

    @Override
    public boolean permiteTransicao(StatusProgramacao statusAtual) {
        return statusAtual == StatusProgramacao.PRODUZIDO;
    }

    @Override
    public void registrarRastreabilidade(Lote lote, Equipamento equipamento,
            RastreabilidadeService rastreabilidadeService) {
        String evento = "O lote " + lote.getNome() + "foi aprovado no equipamento " + equipamento.getNome() + " em "
                + LocalDateTime.now();

        rastreabilidadeService.registrarEventoRastreabilidade(lote, equipamento, StatusRastreabilidade.APROVADO,
                evento);

    }
}
