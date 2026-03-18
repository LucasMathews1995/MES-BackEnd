package com.example.mes.producao.application.dto;

import com.example.mes.producao.domain.Lote;
import com.example.mes.producao.domain.StatusProgramacao;

import java.time.LocalDateTime;

public record ProgramacaoResponseDTO(Long id , String nomeLote , String nomeEquipamento, StatusProgramacao status , LocalDateTime dataHoraProgramada, Integer sequenciaFila,Integer quantidadeConsumida, Integer quantidadeEmEstoque) {
}
