package com.example.mes.producao.application.dto;


import jakarta.validation.constraints.NotNull;

public record ProgramacaoRequestDTO(@NotNull Long loteId ,@NotNull Long equipamentoId , @NotNull Integer quantidadeConsumida) {
}
