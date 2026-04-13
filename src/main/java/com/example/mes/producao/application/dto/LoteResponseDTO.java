package com.example.mes.producao.application.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record LoteResponseDTO (@NotNull Long id, @NotBlank String nomeLote,@NotNull Integer quantidadeDisponivel,@NotNull LocalDateTime dataCriacao,@NotBlank  String descricao){
}
