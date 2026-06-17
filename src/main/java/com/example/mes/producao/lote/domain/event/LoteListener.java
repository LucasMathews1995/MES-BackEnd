package com.example.mes.producao.lote.domain.event;


import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import com.example.mes.producao.lote.domain.StatusLote;
import com.example.mes.producao.lote.domain.strategy.EstrategiaLote;
import com.example.mes.producao.lote.domain.strategy.factory.EstrategiaLoteFactory;
import com.example.mes.producao.programacao.domain.StatusProgramacao;
import com.example.mes.producao.rastreabilidade.domain.Rastreabilidade;
import com.example.mes.producao.rastreabilidade.domain.StatusRastreabilidade;
import com.example.mes.producao.rastreabilidade.infraestructure.persistence.RastreabilidadeRepository;

import jakarta.transaction.Transactional;

@Component
public class LoteListener {

  
    private final EstrategiaLoteFactory estrategia;
    private final RastreabilidadeRepository repository;


    public LoteListener( EstrategiaLoteFactory estrategia,RastreabilidadeRepository repository ) {
     
        this.estrategia = estrategia;
        this.repository = repository;
 
   
    }

    @EventListener
    @Transactional
     public void onLoteEvent(LoteEvent event) {
        EstrategiaLote estrategiaLote = mudarStatusProgramacao(event.statusProgramacao());

        estrategiaLote.processar(event.programacao());
    

    }

    @EventListener
    @Transactional
    public void onLoteEventCreated(LoteCreatedEvent event){
        
        Rastreabilidade rastreabilidade = Rastreabilidade.eventoLoteCriado(StatusRastreabilidade.CRIADO_LOTE,event.lote());
        repository.save(rastreabilidade);
        
    }

    private EstrategiaLote mudarStatusProgramacao(StatusProgramacao statusProgramacao){

        return switch(statusProgramacao){
          case PROGRAMADA -> estrategia.obterEstrategia(StatusLote.RESERVADO);
        case EM_EXECUCAO -> estrategia.obterEstrategia(StatusLote.ABASTECIDO);
           case CONCLUIDA -> estrategia.obterEstrategia(StatusLote.CONSUMIDO);
           default -> throw new IllegalArgumentException("Status de Programação não tratado: " + statusProgramacao);
        };
        
    }
  

}
