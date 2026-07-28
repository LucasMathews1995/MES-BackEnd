package com.example.mes.producao.equipamento.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record EquipamentoPesoUpdateDTO(@NotNull @Min(message = "O valor deve ser no minimo 1000000",value = 1000000)  Long capacidade) {

}
