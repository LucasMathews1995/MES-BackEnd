package com.example.mes.producao.application.service;

import com.example.mes.producao.application.dto.EquipamentoResponseDTO;
import com.example.mes.producao.application.dto.LoteResponseDTO;
import com.example.mes.producao.application.dto.ProgramacaoRequestDTO;
import com.example.mes.producao.application.dto.ProgramacaoResponseDTO;
import com.example.mes.producao.application.mapper.ProgramacaoMapper;
import com.example.mes.producao.domain.Equipamento;
import com.example.mes.producao.domain.Lote;
import com.example.mes.producao.domain.Programacao;
import com.example.mes.producao.infraestructure.EquipamentoRepository;
import com.example.mes.producao.infraestructure.LoteRepository;
import com.example.mes.producao.infraestructure.ProgramacaoRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class ProgramacaoService {

    private final ProgramacaoRepository programacaoRepository;
    private final LoteService loteService;
    private final EquipamentoService equipamentoService;
    private final ProgramacaoMapper programacaoMapper;


    public ProgramacaoResponseDTO inciarProgramacao(ProgramacaoRequestDTO dto){
        Lote lote = loteService.getById(dto.loteId());
        Equipamento equipamento = equipamentoService.getById(dto.equipamentoId());

      Programacao programacao=   programacaoMapper.toEntity(dto);

        loteService.atualizarEstoque(lote.getId(), dto.quantidadeConsumida());
        programacao.setLote(lote);
        programacao.setEquipamento(equipamento);
     Programacao progSalva =    programacaoRepository.save(programacao);

        return programacaoMapper.toDTO(progSalva);

    }

}
