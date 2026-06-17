package com.example.mes.producao.lote.infraestructure.dto;

import java.time.LocalDateTime;

import com.example.mes.producao.lote.domain.Lote;

public record LoteOutputDTO(
    Long id,
    String nome,
    LocalDateTime dataCriacao,
    String descricao
) {
public static LoteOutputDTO fromEntity(Lote lote) {
    return new LoteOutputDTO(
        lote.getId(),
        lote.getNome(),
        lote.getDataCriacao(),
        lote.getDescricao()
    );
}
}
