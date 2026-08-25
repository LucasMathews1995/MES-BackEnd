package com.example.mes.producao.equipamento.dto;

import java.time.LocalDateTime;



import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record EquipamentoRequestDTO(
        @NotBlank(message = "O nome não pode ser vazio") String nome,
        @NotBlank(message = "A sigla não pode ser vazia") String sigla,
        @NotBlank(message = "A descrição não pode ser vazia") String descricao,
       @NotNull(message = "A data de ativação não pode ser nula")  LocalDateTime dataAtivacao,
        @Min(value = 50000, message = "A capacidade deve ser um número maior que 50000.") @Max(value = 1000000000, message = "A capacidade deve ser um número menor que 100000000.") Long capacidade) {

 public EquipamentoRequestDTO {
        if (nome != null) nome = nome.trim().replaceAll("\\s+", " ");
        if (sigla != null) sigla = sigla.trim().replaceAll("\\s+", " ");
    }

    public String getNomeAchatado() {
        if (this.nome == null)
            return null;

        return this.nome.replaceAll("\\s+", "").toLowerCase();
    }
}
