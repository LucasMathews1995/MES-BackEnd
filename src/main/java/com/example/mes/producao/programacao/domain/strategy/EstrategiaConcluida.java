package com.example.mes.producao.programacao.domain.strategy;



import com.example.mes.producao.equipamento.model.Equipamento;
import com.example.mes.producao.programacao.domain.Programacao;
import com.example.mes.producao.programacao.domain.StatusProgramacao;
import com.example.mes.producao.programacao.domain.exceptions.NotFoundProgramacaoException;
import com.example.mes.producao.programacao.infraestructure.persistence.ProgramacaoRepository;

import org.springframework.stereotype.Component;

@Component
public class EstrategiaConcluida implements EstrategiaProgramacao {

    private final ProgramacaoRepository programacaoRepository;

    public EstrategiaConcluida(ProgramacaoRepository programacaoRepository) {
        this.programacaoRepository = programacaoRepository;
    }

    @Override
    public StatusProgramacao getStatusAlvo() {
        return StatusProgramacao.CONCLUIDA;
    }

    @Override
    public void processar(Programacao programacao) {
        
        if (programacao.getStatus() != StatusProgramacao.EM_EXECUCAO) {
            throw new NotFoundProgramacaoException(
                    "Apenas programações no status EM_EXECUCAO podem ser concluídas.");
        }

        programacao.setStatus(StatusProgramacao.CONCLUIDA);
        programacao.setFila(null);

        Equipamento equipamento =  programacao.getEquipamento();
        equipamento.acrescerCapacidade(programacao.getQuantidadeConsumida());
programacaoRepository.save(programacao);


    }
    }


