package com.example.mes.producao.programacao.domain.strategy.normal;

import org.springframework.stereotype.Component;

import com.example.mes.producao.lote.domain.Lote;
import com.example.mes.producao.lote.infraestructure.persistence.LoteRepository;
import com.example.mes.producao.programacao.domain.Programacao;
import com.example.mes.producao.programacao.domain.StatusProgramacao;
import com.example.mes.producao.programacao.infraestructure.persistence.ProgramacaoRepository;

@Component
public class EstrategiaCancelada implements EstrategiaProgramacao {

    private final ProgramacaoRepository programacaoRepository;


    public EstrategiaCancelada(ProgramacaoRepository programacaoRepository) {
        this.programacaoRepository = programacaoRepository;
       
    }

    @Override
    public StatusProgramacao getStatusAlvo() {
        return StatusProgramacao.CANCELADA;
    }

    @Override
    public void processar(Programacao programacao) {
       
     
        programacao.cancelarProgramacao();
programacaoRepository.save(programacao);
    }

}
