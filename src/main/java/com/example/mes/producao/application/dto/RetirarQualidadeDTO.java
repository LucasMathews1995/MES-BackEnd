package com.example.mes.producao.application.dto;

import jakarta.validation.constraints.NotNull;

public record RetirarQualidadeDTO(@NotNull Long OpId,@NotNull Long equipamentoId) {

}
