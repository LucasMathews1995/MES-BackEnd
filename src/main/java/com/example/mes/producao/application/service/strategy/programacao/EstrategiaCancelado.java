package com.example.mes.producao.application.service.strategy.programacao;

import com.example.mes.producao.application.service.RastreabilidadeService;
import com.example.mes.producao.domain.Equipamento;
import com.example.mes.producao.domain.Lote;
import com.example.mes.producao.domain.Programacao;
import com.example.mes.producao.domain.StatusProgramacao;
import com.example.mes.producao.domain.StatusRastreabilidade;

import java.time.LocalDateTime;

import org.springframework.stereotype.Component;

@Component
public class EstrategiaCancelado implements MudancaStatusStrategy{

    private final RastreabilidadeService rastreabilidadeService;
    
    public EstrategiaCancelado(RastreabilidadeService rastreabilidadeService) {
        this.rastreabilidadeService = rastreabilidadeService;
    }

    @Override
    public StatusProgramacao getStatusAlvo() {
        return StatusProgramacao.CANCELADO;
    }

    @Override
    public void processar(Programacao programacao, Lote lote, Equipamento equipamento) {
        if (programacao.getStatus() != StatusProgramacao.CRIADO) {
            throw new IllegalStateException("Apenas programações no status CRIADO podem ser canceladas.");
        }

        programacao.setStatus(StatusProgramacao.CANCELADO);



        String evento = String.format("O lote %s foi cancelado no equipamento %s em %s",
                lote.getNome(), equipamento.getNome(), LocalDateTime.now());

        rastreabilidadeService.registrarEventoRastreabilidade(lote, equipamento, StatusRastreabilidade.CANCELADO,
                evento);
    }


  
    
}
