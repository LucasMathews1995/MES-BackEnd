package com.example.mes.producao.application.dto;

import com.example.mes.producao.domain.StatusProgramacao;

import java.time.LocalDateTime;

public record ProgramacaoOrdemProducaoDTO(
        Long idProgramacao,
        LocalDateTime dataHoraProgramada,
        Integer fila,
        Integer quantidadeConsumida,
        String nome,
        StatusProgramacao status,// Opcional, se quiser mostrar
        String numeroOP
        // Aqui é o que você queria!
) {}