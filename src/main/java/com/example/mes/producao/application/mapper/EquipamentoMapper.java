package com.example.mes.producao.application.mapper;

import com.example.mes.producao.application.dto.EquipamentoRequestDTO;
import com.example.mes.producao.application.dto.EquipamentoResponseDTO;
import com.example.mes.producao.domain.Equipamento;
import org.springframework.stereotype.Component;

@Component
public class EquipamentoMapper {


    public Equipamento toEntity(EquipamentoRequestDTO dto){
        return new Equipamento(dto.nome(),dto.sigla(),dto.descricao(),dto.dataAtivacao());
    }


    public EquipamentoResponseDTO toDTO(Equipamento entity){
        return new EquipamentoResponseDTO(entity.getId(),entity.getNome(),entity.getSigla(),entity.getDescricao(),entity.getDataAtivacao(),entity.getDataParado(),entity.isAtivo());
    }
}
