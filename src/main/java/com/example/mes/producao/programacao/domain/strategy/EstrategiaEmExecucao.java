package com.example.mes.producao.programacao.domain.strategy;

import com.example.mes.producao.equipamento.model.Equipamento;
import com.example.mes.producao.programacao.domain.Programacao;
import com.example.mes.producao.programacao.domain.StatusProgramacao;
import com.example.mes.producao.programacao.domain.exceptions.ProgramacaoNotValidException;
import com.example.mes.producao.programacao.infraestructure.persistence.ProgramacaoRepository;

import org.springframework.stereotype.Component;

@Component
public class EstrategiaEmExecucao implements EstrategiaProgramacao {

    private final ProgramacaoRepository programacaoRepository;

    public EstrategiaEmExecucao(ProgramacaoRepository programacaoRepository) {
        this.programacaoRepository = programacaoRepository;
    }

    @Override
    public StatusProgramacao getStatusAlvo() {
        return StatusProgramacao.EM_EXECUCAO;
    }

    @Override
    public void processar(Programacao programacao) {
        if (programacao.getStatus() != StatusProgramacao.PROGRAMADA) {
            throw new ProgramacaoNotValidException(
                    "A programação deve estar no status PROGRAMADA para ser colocada em execução.");
        }

        programacao.setStatus(StatusProgramacao.EM_EXECUCAO);
  

        Equipamento equipamento = programacao.getEquipamento();

        equipamento.diminuirCapacidade(programacao.getQuantidadeConsumida());
        programacaoRepository.save(programacao);

    }

}
