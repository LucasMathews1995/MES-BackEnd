package com.example.mes.producao.application.dto;

import java.time.LocalDateTime;


public record EquipamentoResponseDTO (Long id,String nome, String sigla, String descricao, LocalDateTime dataAtivacao, LocalDateTime dataParado, Boolean isAtivo) {
}
