package com.example.mes.producao.programacao.domain.strategy.normal;


import org.springframework.stereotype.Component;


import com.example.mes.producao.programacao.domain.Programacao;
import com.example.mes.producao.programacao.domain.StatusProgramacao;
import com.example.mes.producao.programacao.domain.exceptions.ProgramacaoNotValidException;
import com.example.mes.producao.programacao.infraestructure.persistence.ProgramacaoRepository;

@Component
public class EstrategiaProgramada implements EstrategiaProgramacao {

    private final ProgramacaoRepository programacaoRepository;

    public EstrategiaProgramada(ProgramacaoRepository programacaoRepository) {
        this.programacaoRepository = programacaoRepository;
    }
    @Override
    public StatusProgramacao getStatusAlvo() {
     return StatusProgramacao.PROGRAMADA;
    }

    @Override
    public void processar(Programacao programacao) {
       
        Integer ultimaFila = programacaoRepository.findMaxOrdemByEquipamentoAndStatus(
                                programacao.getEquipamento().getId(),
                                StatusProgramacao.PROGRAMADA);
            
        if(!programacao.getStatus().podeMudarPara(StatusProgramacao.PROGRAMADA)) {
            throw new ProgramacaoNotValidException("Apenas programações no status CRIADO podem ser programadas.");

        }


     

        programacao.programarLote(ultimaFila);
       
      

    }



    
}