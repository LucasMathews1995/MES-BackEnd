package com.example.mes.producao.lote.domain.strategy.loteStrategy;


import org.springframework.stereotype.Component;
import com.example.mes.producao.lote.domain.Lote;
import com.example.mes.producao.lote.domain.StatusLote;
import com.example.mes.producao.programacao.domain.Programacao;
import com.example.mes.producao.programacao.domain.StatusProgramacao;
import com.example.mes.producao.programacao.infraestructure.persistence.ProgramacaoRepository;

@Component
public class EstrategiaLoteConsumido implements EstrategiaLote {
  
    private final ProgramacaoRepository programacaoRepository;

    public EstrategiaLoteConsumido(ProgramacaoRepository programacaoRepository) {
        this.programacaoRepository = programacaoRepository;
    }

    @Override
    public StatusLote getStatusAlvo() {
        return StatusLote.CONSUMIDO;
    }

    @Override
    public void processar(Programacao programacao) {

        Lote loteConsumido = programacao.getLoteConsumido();
        Lote loteProduzido = programacao.getLoteProduzido();
        loteProduzido.desabastecerLote();
        if(programacaoRepository.todasProgramacoesEstaoConcluidas(loteConsumido.getId(), StatusProgramacao.CONCLUIDA)){
             loteConsumido.consumirLote();
        }else {
            loteConsumido.desabastecerLote();
        }
       


        
       

    }

}
