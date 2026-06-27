package com.example.mes.producao.programacao.domain.strategy;

import com.example.mes.producao.equipamento.model.Equipamento;
import com.example.mes.producao.programacao.domain.Programacao;
import com.example.mes.producao.programacao.domain.StatusProgramacao;
import com.example.mes.producao.programacao.domain.exceptions.ProgramacaoNotValidException;

import org.springframework.stereotype.Component;

@Component
public class EstrategiaEmExecucao implements EstrategiaProgramacao {

    @Override
    public StatusProgramacao getStatusAlvo() {
        return StatusProgramacao.EM_EXECUCAO;
    }

    @Override
    public void processar(Programacao programacao, Integer ultimaFila) {
        if (programacao.getStatus() != StatusProgramacao.PROGRAMADA) {
            throw new ProgramacaoNotValidException(
                    "A programação deve estar no status PROGRAMADA para ser colocada em execução.");
        }

        programacao.setStatus(StatusProgramacao.EM_EXECUCAO);
  

        Equipamento equipamento = programacao.getEquipamento();

        equipamento.diminuirCapacidade(programacao.getQuantidadeConsumida());

    }

}
