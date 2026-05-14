package com.example.mes.producao.application.dto;

import java.time.LocalDateTime;

import com.example.mes.producao.domain.StatusRastreabilidade;

public record RastreabilidadeDTO(
        Long id,
        String lote,
        String equipamento,
        LocalDateTime dataHoraEntrada,
        LocalDateTime dataHoraSaida,
        StatusRastreabilidade status,
        String evento) {
}