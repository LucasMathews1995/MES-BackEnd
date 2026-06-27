package com.example.mes.producao.ordemproducao.dto;

import jakarta.validation.constraints.NotNull;

public record OrdemProducaoInputDTO(@NotNull(message = "O ID do equipamento não pode ser nulo") Long equipamentoId ) {

}
