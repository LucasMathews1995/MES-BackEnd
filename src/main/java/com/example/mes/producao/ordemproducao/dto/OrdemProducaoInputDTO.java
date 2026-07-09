package com.example.mes.producao.ordemproducao.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record OrdemProducaoInputDTO(@NotNull(message = "O ID do equipamento não pode ser nulo") Long equipamentoId ,@NotNull(message = "A capacidade máxima não pode ser nula")
@Positive(message = "A capacidade máxima deve ser um valor positivo")
Long capacidadeMaxima) {

}
