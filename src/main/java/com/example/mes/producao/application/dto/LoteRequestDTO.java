package com.example.mes.producao.application.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

public record LoteRequestDTO (
    @NotNull(message = "A quantidade não pode ser nula")
    @Positive(message = "A quantidade deve ser maior que zero") 
    @Max(value = 99999, message = "A quantidade não pode ser maior que 99999")
     Integer quantidadeDisponivel,
    @NotNull(message = "A data de criação não pode ser nula")
     @NotNull LocalDateTime dataCriacao,
     @NotBlank(message = "A descrição não pode ser vazia")
    @Size(max = 255, message = "A descrição deve ter no máximo 255 caracteres")
     @NotBlank String descricao){
}
