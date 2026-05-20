package com.example.mes.producao.application.dto;

import com.example.mes.producao.domain.StatusOP;

import jakarta.validation.constraints.NotNull;

public record OrdemProducaoRequestDTO(@NotNull StatusOP status) {

}
