package com.example.mes.producao.programacao.infraestructure.dto;

import java.time.LocalDateTime;

import com.example.mes.producao.programacao.domain.Programacao;

public record ProgramacaoOutputDTO (Long loteId , Long equipamentoId, LocalDateTime dataHoraProgramada, Long quantidadeConsumida) {

    public static ProgramacaoOutputDTO fromEntity(Programacao programacao) {
        return new ProgramacaoOutputDTO(
            programacao.getLoteConsumido().getId(),
            programacao.getEquipamento().getId(),
            programacao.getDataHoraProgramada(),
            programacao.getQuantidadeConsumida()
        );
    }

}
