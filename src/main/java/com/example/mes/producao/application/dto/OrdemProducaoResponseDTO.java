package com.example.mes.producao.application.dto;

import com.example.mes.producao.domain.StatusOP;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;


public record OrdemProducaoResponseDTO(@NotBlank String numeroOP, @NotNull Long ordemVendaId , @NotBlank StatusOP status , @NotNull LocalDateTime dataCriacao,@NotNull LocalDateTime dataEncerramento) {
}
