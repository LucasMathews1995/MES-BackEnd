package com.example.mes.producao.application.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;


public record EquipamentoResponseDTO (@NotNull Long id, @NotBlank String nome,@NotBlank String sigla,@NotBlank String descricao,@NotNull LocalDateTime dataAtivacao, @NotNull LocalDateTime dataParado, @NotNull Boolean isAtivo) {
}
