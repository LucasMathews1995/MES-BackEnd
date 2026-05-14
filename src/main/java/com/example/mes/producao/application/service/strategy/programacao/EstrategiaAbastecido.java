package com.example.mes.producao.application.service.strategy.programacao;

import com.example.mes.producao.application.dto.ProgramacaoRequestDTO;
import com.example.mes.producao.application.service.LoteService;
import com.example.mes.producao.application.service.ProgramacaoService;
import com.example.mes.producao.application.service.RastreabilidadeService;
import com.example.mes.producao.domain.Equipamento;
import com.example.mes.producao.domain.Lote;
import com.example.mes.producao.domain.Programacao;
import com.example.mes.producao.domain.Rastreabilidade;
import com.example.mes.producao.domain.StatusProgramacao;
import com.example.mes.producao.domain.StatusRastreabilidade;

import java.time.LocalDateTime;

import org.springframework.stereotype.Component;

@Component
public class EstrategiaAbastecido implements MudancaStatusStrategy {
    @Override
    public StatusProgramacao getStatusAlvo() {
        return StatusProgramacao.ABASTECIDO;
    }

    @Override
    public Lote processarLote(LoteService loteService, ProgramacaoRequestDTO dto) {
        return loteService.abastecerLote(dto.loteId());
    }

    @Override
    public void finalizarProgramacao(Programacao programacao, ProgramacaoService programacaoService) {
        programacao.setStatus(StatusProgramacao.ABASTECIDO);
    }

    @Override
    public boolean permiteTransicao(StatusProgramacao statusAtual) {
        return statusAtual == StatusProgramacao.PROGRAMADO;
    }

    @Override
    public void registrarRastreabilidade(Lote lote, Equipamento equipamento,
            RastreabilidadeService rastreabilidadeService) {
                
        String evento = "O lote " + lote.getNome() + "foi abastecido no equipamento " + equipamento.getNome() + " em "
                + LocalDateTime.now();

        rastreabilidadeService.registrarEventoDeAbastecimento(lote, equipamento, StatusRastreabilidade.ABASTECIDO,
                evento);
    }
}
