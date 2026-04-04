package com.example.mes.producao.application.dto;


import jakarta.validation.constraints.NotNull;

public record ProgramacaoRequestDTO(Long loteId, Long equipamentoId , @NotNull Integer quantidadeConsumida) {
}
