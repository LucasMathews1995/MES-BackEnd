package com.example.mes.producao.lote.domain.strategy.loteStrategy;

import java.util.List;

import org.springframework.stereotype.Component;


import com.example.mes.producao.lote.domain.StatusLote;
import com.example.mes.producao.lote.infraestructure.persistence.LoteRepository;
import com.example.mes.producao.programacao.domain.Programacao;

@Component
public class EstrategiaLoteAbastecido implements EstrategiaLote {

    private final LoteRepository loteRepository;
    public EstrategiaLoteAbastecido(LoteRepository loteRepository) {
        this.loteRepository = loteRepository;
    }

    @Override
    public StatusLote getStatusAlvo() {
        return StatusLote.ABASTECIDO;
    }

    @Override
    public void processar(Programacao programacao) {
        programacao.getLoteConsumido().abastecerLote();
        loteRepository.saveAll(List.of(programacao.getLoteConsumido(), programacao.getLoteProduzido()));
    }

}
