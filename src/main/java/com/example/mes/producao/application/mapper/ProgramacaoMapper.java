package com.example.mes.producao.application.mapper;


import com.example.mes.producao.application.dto.OrdemProducaoResponseDTO;
import com.example.mes.producao.application.dto.ProgramacaoRequestDTO;
import com.example.mes.producao.application.dto.ProgramacaoResponseDTO;

import com.example.mes.producao.domain.Programacao;
import com.example.mes.producao.domain.StatusProgramacao;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;


@Component
@RequiredArgsConstructor
public class ProgramacaoMapper {

    private final OrdemProducaoMapper ordemProducaoMapper;


    public Programacao toEntity(ProgramacaoRequestDTO dto) {

        return new Programacao(StatusProgramacao.CRIADA, dto.quantidadeConsumida());


    }


    public ProgramacaoResponseDTO toDTO(Programacao entity) {


return null;

    }


}
