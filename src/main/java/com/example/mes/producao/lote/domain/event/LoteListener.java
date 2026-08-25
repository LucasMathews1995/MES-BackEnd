package com.example.mes.producao.lote.domain.event;

import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import com.example.mes.producao.lote.domain.StatusLote;
import com.example.mes.producao.lote.domain.strategy.factory.EstrategiaLoteFactory;
import com.example.mes.producao.lote.domain.strategy.loteStrategy.EstrategiaLote;
import com.example.mes.producao.programacao.domain.StatusProgramacao;
import com.example.mes.rastreabilidade.domain.Rastreabilidade;
import com.example.mes.rastreabilidade.domain.StatusRastreabilidade;
import com.example.mes.rastreabilidade.infraestructure.persistence.RastreabilidadeRepository;

@Component
public class LoteListener {

    private final EstrategiaLoteFactory estrategia;
    private final RastreabilidadeRepository repository;

    public LoteListener(EstrategiaLoteFactory estrategia, RastreabilidadeRepository repository) {

        this.estrategia = estrategia;
        this.repository = repository;

    }

    @EventListener
    public void onLoteEvent(LoteEvent event) {
        EstrategiaLote estrategiaLote = mudarStatusProgramacao(event.statusProgramacao());

        estrategiaLote.processar(event.programacao());

    }

    @EventListener
    public void onLoteEventCreated(LoteCreatedEvent event) {

        Rastreabilidade rastreabilidade = Rastreabilidade.eventoLoteCriado(StatusRastreabilidade.CRIADO_LOTE,
                event.lote());
        repository.save(rastreabilidade);

    }

    private EstrategiaLote mudarStatusProgramacao(StatusProgramacao statusProgramacao) {

        return switch (statusProgramacao) {
            case PROGRAMADA -> estrategia.obterEstrategia(StatusLote.RESERVADO);
            case EM_EXECUCAO -> estrategia.obterEstrategia(StatusLote.ABASTECIDO);
            case CONCLUIDA -> estrategia.obterEstrategia(StatusLote.CONSUMIDO);
            case CANCELADA -> estrategia.obterEstrategia(StatusLote.DESABASTECIDO);
            case QUALIDADE -> estrategia.obterEstrategia(StatusLote.QUALIDADE);
            case CRIADA -> estrategia.obterEstrategia(StatusLote.LIBERADO);
            case DESABASTECIDO -> estrategia.obterEstrategia(StatusLote.DESABASTECIDO);
            
            
            default -> throw new IllegalArgumentException("Status de Programação não tratado: " + statusProgramacao);
        };

    }

}
