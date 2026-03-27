package com.example.mes.producao.application.dto;

import com.example.mes.producao.domain.StatusOP;

import java.time.LocalDateTime;


public record OrdemProducaoResponseDTO(String numeroOP,Long ordemVendaId ,StatusOP status , LocalDateTime dataCriacao, LocalDateTime dataEncerramento) {
}
