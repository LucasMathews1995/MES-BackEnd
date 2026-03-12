package com.example.mes.producao.application.dto;

import com.example.mes.producao.domain.StatusProgramacao;

import java.time.LocalDateTime;

public record ProgramacaoRequestDTO(Long loteId , Long equipamentoId ,  Integer sequenciaFila , Integer quantidadeConsumida) {
}
