package com.example.mes.producao.application.dto;


import com.example.mes.producao.domain.StatusOP;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record OrdemProducaoRequestDTO(@NotNull Long id , @NotNull Long ordemVendaId, @NotBlank String numeroOP, @NotBlank StatusOP status, @NotNull LocalDateTime dataEncerramento) {
}
