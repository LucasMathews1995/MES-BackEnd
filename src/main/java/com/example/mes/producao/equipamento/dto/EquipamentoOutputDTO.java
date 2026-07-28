package com.example.mes.producao.equipamento.dto;

import com.example.mes.producao.equipamento.model.Equipamento;

public record EquipamentoOutputDTO(Long id, String nome, String sigla, String descricao) {


    public static EquipamentoOutputDTO fromEntity(Equipamento equipamento){

        return new EquipamentoOutputDTO(equipamento.getId(), 
        equipamento.getNome(), 
        equipamento.getSigla(), 
        equipamento.getDescricao()) ;

    }

}
