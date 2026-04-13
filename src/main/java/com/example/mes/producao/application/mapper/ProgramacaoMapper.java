package com.example.mes.producao.application.mapper;



import com.example.mes.producao.api.exception.NotProgramacaoValidException;
import com.example.mes.producao.application.dto.ProgramacaoResumoResponseDTO;
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

        return new Programacao(lote, equipamento,StatusProgramacao.CRIADO, dto.quantidadeConsumida());


    }


    public ProgramacaoResponseDTO toDTODetalhe(Programacao entity,Lote lote, Equipamento equipamento) {


        if(lote==null || equipamento==null){
            throw new NotProgramacaoValidException("Lote ou Equipamento deve ser informado.");
        }


    return  new ProgramacaoResponseDTO(entity.getId(),
            entity.getStatus(),
            entity.getDataHoraProgramada(),
            entity.getQuantidadeConsumida(),
            lote.getId(),
            lote.getNome(),
            equipamento.getId(),
            equipamento.getNome());



    }

    public ProgramacaoResumoResponseDTO toDTOResumo(Programacao entity,Lote lote, Equipamento equipamento) {
            if(lote==null || equipamento==null){
                throw new NotProgramacaoValidException("Lote ou Equipamento deve ser informado.");
            }
        return  new ProgramacaoResumoResponseDTO(entity.getId(),
                entity.getStatus(),
                entity.getDataHoraProgramada(),
                entity.getQuantidadeConsumida(),lote.getNome(),equipamento.getNome());


    }
    public ProgramacaoResumoResponseDTO toDTOResumo(Programacao entity){
        if(entity==null){
            throw new NotProgramacaoValidException("Programacao deve ser informado.");
        }

        return  new ProgramacaoResumoResponseDTO(entity.getId(),
                entity.getStatus(),
                entity.getDataHoraProgramada(),
                entity.getQuantidadeConsumida(),entity.getEquipamento().getNome(),entity.getLote().getNome());



}
}
