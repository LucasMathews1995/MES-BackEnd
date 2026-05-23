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
public class EstrategiaFinalizado implements MudancaStatusStrategy {

    private final RastreabilidadeService rastreabilidadeService;

    public EstrategiaFinalizado(RastreabilidadeService rastreabilidadeService) {
        this.rastreabilidadeService = rastreabilidadeService;
    }

    @Override
    public StatusProgramacao getStatusAlvo() {
        return StatusProgramacao.FINALIZADO;
    }

    @Override
    public void processar(Programacao programacao, Lote lote, Equipamento equipamento) {
        if (programacao.getStatus() != StatusProgramacao.EM_EXECUCAO) {
            throw new NotProgramacaoValidException(
                    "Apenas programações no status EM_EXECUCAO podem ser finalizadas.");
        }

        programacao.setStatus(StatusProgramacao.FINALIZADO);

        String evento = String.format("O lote %s foi finalizado no equipamento %s em %s",
                lote.getNome(), equipamento.getNome(), LocalDateTime.now());

        rastreabilidadeService.registrarEventoRastreabilidade(lote, equipamento, StatusRastreabilidade.FINALIZADO,
                evento);
    }

}
