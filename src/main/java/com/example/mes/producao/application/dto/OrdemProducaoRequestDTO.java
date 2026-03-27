package com.example.mes.producao.application.dto;

import com.example.mes.producao.domain.Lote;
import com.example.mes.producao.domain.StatusOP;

import java.time.LocalDateTime;

public record OrdemProducaoRequestDTO(Long id , Long ordemVendaId, String numeroOP, StatusOP status, LocalDateTime dataEncerramento) {
}
