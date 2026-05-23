package com.example.mes.producao.application.dto;


import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record ProgramacaoRequestDTO(@NotNull Long loteId,@NotNull Long equipamentoId, 
    @NotNull(message = "A quantidade não pode ser nula")
    @Positive(message = "A quantidade deve ser maior que zero") 
    @Max(value = 99999, message = "A quantidade não pode ser maior que 99999") 
    Integer quantidadeConsumida) {
}
