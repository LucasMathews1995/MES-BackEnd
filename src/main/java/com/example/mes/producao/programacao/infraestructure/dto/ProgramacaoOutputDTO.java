package com.example.mes.producao.programacao.infraestructure.dto;

import java.time.LocalDateTime;

import com.example.mes.producao.programacao.domain.Programacao;
import com.example.mes.producao.programacao.domain.StatusProgramacao;

public record ProgramacaoOutputDTO (Long id, LocalDateTime dataHoraProgramada, Long quantidadeConsumida,StatusProgramacao status) {

    public static ProgramacaoOutputDTO fromEntity(Programacao programacao) {
        return new ProgramacaoOutputDTO(
            programacao.getId(),
            programacao.getDataHoraProgramada(),
            programacao.getQuantidadeConsumida(),
            programacao.getStatus()
        );
    }

}

   



