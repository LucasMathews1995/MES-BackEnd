package com.example.mes.producao.application.dto;

import java.time.LocalDateTime;

public record LoteResponseDTO (Long id,String nomeLote,Integer quantidadeDisponivel, LocalDateTime dataCriacao, String descricao){
}
