package com.example.mes.producao.lote.domain.strategy;

import org.springframework.stereotype.Component;

import com.example.mes.producao.lote.domain.Lote;
import com.example.mes.producao.lote.domain.StatusLote;
import com.example.mes.producao.lote.infraestructure.persistence.LoteRepository;
import com.example.mes.producao.programacao.domain.Programacao;


@Component
public class EstrategiaLoteQualidade implements EstrategiaLote {
    private final LoteRepository loteRepository;

    public EstrategiaLoteQualidade(LoteRepository loteRepository) {
        this.loteRepository = loteRepository;
    }

    @Override
    public StatusLote getStatusAlvo() {
        return StatusLote.QUALIDADE;
    }

    @Override
    public void processar(Programacao programacao) {
        Lote loteConsumido = programacao.getLoteConsumido();
        Lote loteProduzido = programacao.getLoteProduzido().rejeitarLoteProduzido();

        loteConsumido.rejeitarLoteConsumido(programacao.getQuantidadeConsumida());

        
        loteRepository.delete(loteProduzido);
        loteRepository.save(loteConsumido);
        
    }


    }


