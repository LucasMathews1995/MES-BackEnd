package com.example.mes.producao.application.dto;

import com.example.mes.producao.domain.StatusProgramacao;
import java.time.LocalDateTime;


public record ProgramacaoResponseDTO(Long id , OrdemProducaoResponseDTO listaOrdemProducao,String nomeEquipamento, StatusProgramacao status , LocalDateTime dataHoraProgramada, Integer quantidadeConsumida, String nomeOP) {
}
