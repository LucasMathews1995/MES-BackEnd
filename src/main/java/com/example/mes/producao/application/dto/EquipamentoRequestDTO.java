package com.example.mes.producao.application.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

public record EquipamentoRequestDTO(
        @NotBlank(message = "O nome do equipamento é obrigatório.") @Size(min = 3, max = 50, message = "O nome deve ter entre 3 e 50 caracteres.") @Pattern(regexp = "^[A-Za-zÀ-ÖØ-öø-ÿ0-9]+(\\s[A-Za-zÀ-ÖØ-öø-ÿ0-9]+)*$", message = "O nome do equipamento não pode conter espaços duplos, espaços nas pontas ou caracteres inválidos.") String nome,
        @NotBlank(message = "A sigla do equipamento é obrigatória.") @Size(max = 3, min = 3, message = "O nome deve ter 3 caracteres.") String sigla,
        @NotBlank(message = "A descrição do equipamento é obrigatória.") String descricao,
        @NotNull LocalDateTime dataAtivacao) {
}
