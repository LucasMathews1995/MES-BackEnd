package com.example.mes.producao.application.mapper;



import com.example.mes.producao.application.dto.ProgramacaoRequestDTO;
import com.example.mes.producao.application.dto.ProgramacaoResponseDTO;

import com.example.mes.producao.domain.Equipamento;
import com.example.mes.producao.domain.Lote;
import com.example.mes.producao.domain.Programacao;
import com.example.mes.producao.domain.StatusProgramacao;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;



@Component
@RequiredArgsConstructor
public class ProgramacaoMapper {




    public Programacao toEntity(Lote lote, Equipamento equipamento, ProgramacaoRequestDTO dto) {

        return new Programacao(lote, equipamento,StatusProgramacao.CRIADA, dto.quantidadeConsumida());


    }


    public ProgramacaoResponseDTO toDTO(Programacao entity) {

    return  new ProgramacaoResponseDTO(entity.getId(),entity.getEquipamento().getNome(),entity.getStatus(),entity.getDataHoraProgramada(),entity.getQuantidadeConsumida(),entity.getLote().getNome());


    }


}
