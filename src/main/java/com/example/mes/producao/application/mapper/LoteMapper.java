package com.example.mes.producao.application.mapper;

import com.example.mes.producao.application.dto.LoteRequestDTO;
import com.example.mes.producao.application.dto.LoteResponseDTO;
import com.example.mes.producao.domain.Lote;
import org.springframework.stereotype.Component;

@Component
public class LoteMapper {

    public Lote toEntity(LoteRequestDTO dto) {

        return new Lote(dto.quantidadeDisponivel(),dto.dataCriacao(),dto.descricao());
    }


    public LoteResponseDTO toDTO(Lote lote) {
        return new LoteResponseDTO(lote.getId(),lote.getNome(),lote.getQuantidadeDisponivel(),lote.getDataCriacao(),lote.getDescricao());
    }
}
