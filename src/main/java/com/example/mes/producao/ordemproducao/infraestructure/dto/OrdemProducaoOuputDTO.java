package com.example.mes.producao.ordemproducao.infraestructure.dto;

import java.time.LocalDateTime;
import com.example.mes.producao.ordemproducao.domain.OrdemProducao;
import com.example.mes.producao.ordemproducao.domain.StatusOP;

public record OrdemProducaoOuputDTO( Long id,String numeroOP,StatusOP statusOP , LocalDateTime dataCriacao, LocalDateTime dataEncerramento) {



  public static OrdemProducaoOuputDTO fromEntity(OrdemProducao op) {
    return new OrdemProducaoOuputDTO(
        op.getId(),
        op.getNumeroOP(),
        op.getStatus(),
        op.getDataCriacao(),
        op.getDataEncerramento()
    );
}

}
