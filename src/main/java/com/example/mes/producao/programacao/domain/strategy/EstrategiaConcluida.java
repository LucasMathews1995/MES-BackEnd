package com.example.mes.producao.programacao.domain.strategy;



import com.example.mes.producao.programacao.domain.Programacao;
import com.example.mes.producao.programacao.domain.StatusProgramacao;
import com.example.mes.producao.programacao.domain.exceptions.NotFoundProgramacaoException;
import org.springframework.stereotype.Component;

@Component
public class EstrategiaConcluida implements EstrategiaProgramacao {


    @Override
    public StatusProgramacao getStatusAlvo() {
        return StatusProgramacao.CONCLUIDA;
    }

    @Override
    public void processar(Programacao programacao,Integer ultimaFila) {
        if (programacao.getStatus() != StatusProgramacao.EM_EXECUCAO) {
            throw new NotFoundProgramacaoException(
                    "Apenas programações no status EM_EXECUCAO podem ser concluídas.");
        }

        programacao.setStatus(StatusProgramacao.CONCLUIDA);
        programacao.setFila(ultimaFila );

    }
    }


