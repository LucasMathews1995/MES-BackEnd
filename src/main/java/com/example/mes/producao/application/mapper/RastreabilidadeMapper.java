package com.example.mes.producao.application.mapper;


import org.springframework.stereotype.Component;

import com.example.mes.producao.application.dto.RastreabilidadeDTO;
import com.example.mes.producao.domain.Rastreabilidade;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class RastreabilidadeMapper {
    


    public RastreabilidadeDTO toDTO(Rastreabilidade rastreabilidade) {
        RastreabilidadeDTO dto = new RastreabilidadeDTO(rastreabilidade.getId(),
        rastreabilidade.getLote().getNome(),
        rastreabilidade.getEquipamento().getNome(),
        rastreabilidade.getDataHoraEntrada(),
        rastreabilidade.getDataHoraSaida(),
        rastreabilidade.getStatus(),
        rastreabilidade.getEvento());

        return dto;
    }
}
