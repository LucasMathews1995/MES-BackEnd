package com.example.mes.producao.lote.infraestructure.dto;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonProperty;

public record CriarLoteInputDTO( @JsonProperty("quantidade") Integer quantidade, @JsonProperty("dataHoraInicio") LocalDateTime dataHoraInicio, @JsonProperty("descricao") String descricao) {

}
