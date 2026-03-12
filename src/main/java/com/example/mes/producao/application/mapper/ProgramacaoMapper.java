package com.example.mes.producao.application.mapper;

import com.example.mes.producao.application.dto.EquipamentoResponseDTO;
import com.example.mes.producao.application.dto.LoteResponseDTO;
import com.example.mes.producao.application.dto.ProgramacaoRequestDTO;
import com.example.mes.producao.application.dto.ProgramacaoResponseDTO;
import com.example.mes.producao.application.service.ProgramacaoService;
import com.example.mes.producao.domain.Programacao;
import com.example.mes.producao.domain.StatusProgramacao;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class ProgramacaoMapper {

    public Programacao toEntity(ProgramacaoRequestDTO dto) {

        return new Programacao(StatusProgramacao.CRIADA, LocalDateTime.now(),dto.sequenciaFila(),dto.quantidadeConsumida());


    }


    public ProgramacaoResponseDTO toDTO(Programacao entity) {
return new ProgramacaoResponseDTO();
    }
}
