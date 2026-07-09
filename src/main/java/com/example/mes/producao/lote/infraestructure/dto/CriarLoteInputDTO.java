package com.example.mes.producao.lote.infraestructure.dto;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record CriarLoteInputDTO(@NotNull @Positive @JsonProperty("quantidade") Long quantidade, @JsonProperty("dataHoraInicio") @NotBlank LocalDateTime dataHoraInicio, @NotBlank @JsonProperty("descricao") String descricao) {

}
