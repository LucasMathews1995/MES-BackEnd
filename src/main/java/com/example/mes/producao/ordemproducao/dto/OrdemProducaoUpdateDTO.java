package com.example.mes.producao.ordemproducao.dto;

import jakarta.validation.constraints.NotNull;

public record OrdemProducaoUpdateDTO(@NotNull Long capacidadeMaxima) {

}
