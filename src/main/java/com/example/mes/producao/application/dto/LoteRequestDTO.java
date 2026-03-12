package com.example.mes.producao.application.dto;

import java.time.LocalDateTime;

public record LoteRequestDTO ( Integer quantidadeDisponivel, LocalDateTime dataCriacao, String descricao){
}
