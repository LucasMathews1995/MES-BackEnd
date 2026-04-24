package com.example.mes.producao.application.dto;

import com.example.mes.producao.domain.StatusProgramacao;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;


public record ProgramacaoResponseDTO(@NotNull Long id,
                                     @NotBlank StatusProgramacao status, @NotNull LocalDateTime dataHoraProgramada,
                                     @NotNull  Integer quantidadeConsumida,
                                     @NotNull Integer fila,
                                     @NotNull Long loteId,@NotBlank String numeroLote,
                                     @NotNull Long equipamentoId,
                                     @NotBlank String equipamentoNome) {
}
