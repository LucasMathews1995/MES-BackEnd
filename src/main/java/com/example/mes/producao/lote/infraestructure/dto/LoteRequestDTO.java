package com.example.mes.producao.lote.infraestructure.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

public record LoteRequestDTO(Long loteId, Long programacaoId, @Min(value = 50000, message = "A capacidade deve ser um número maior que 50000.") @Max(value = 1000000000, message = "A capacidade deve ser um número menor que 100000000.") Long quantidade) {

}
