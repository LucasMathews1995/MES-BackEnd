package com.example.mes.producao.application.service.strategy.programacao;

import com.example.mes.producao.api.exception.NotProgramacaoValidException;

import com.example.mes.producao.application.service.RastreabilidadeService;
import com.example.mes.producao.domain.Equipamento;
import com.example.mes.producao.domain.Lote;
import com.example.mes.producao.domain.Programacao;
import com.example.mes.producao.domain.StatusProgramacao;
import com.example.mes.producao.domain.StatusRastreabilidade;

import java.time.LocalDateTime;

import org.springframework.stereotype.Component;

@Component
public class EstrategiaPausado implements MudancaStatusStrategy {

    private final RastreabilidadeService rastreabilidadeService;

    public EstrategiaPausado(RastreabilidadeService rastreabilidadeService) {
        this.rastreabilidadeService = rastreabilidadeService;
    }

    @Override
    public StatusProgramacao getStatusAlvo() {
        return StatusProgramacao.PAUSADO;
    }

        @Override
    public void processar(Programacao programacao, Lote lote, Equipamento equipamento) {
        if (programacao.getStatus() != StatusProgramacao.EM_EXECUCAO) {
            throw new NotProgramacaoValidException(
                    "Apenas programações no status EM_EXECUCAO podem ser pausadas.");
        }

        programacao.setStatus(StatusProgramacao.PAUSADO);

        String evento = String.format("O lote %s foi pausado no equipamento %s em %s",
                lote.getNome(), equipamento.getNome(), LocalDateTime.now());

        rastreabilidadeService.registrarEventoRastreabilidade(lote, equipamento, StatusRastreabilidade.PAUSADO,
                evento);
    }

}
