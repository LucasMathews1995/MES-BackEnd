package com.example.mes.producao.ordemproducao.dto;

import java.util.List;

public record OrdemProducaoPaginadaDTO(
    List<OrdemProducaoOuputDTO> itens,
    boolean temProximaPagina
) {
}
