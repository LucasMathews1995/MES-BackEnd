package com.example.mes.producao.application.service;

import com.example.mes.producao.api.exception.ProgramacaoNotFoundException;
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
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@AllArgsConstructor
public class ProgramacaoService {

    private final ProgramacaoRepository programacaoRepository;
    private final LoteService loteService;
    private final EquipamentoService equipamentoService;
    private final ProgramacaoMapper programacaoMapper;

@Transactional
public ProgramacaoResponseDTO inciarProgramacao(ProgramacaoRequestDTO dto) {
    Lote lote = loteService.getById(dto.loteId());
    Equipamento equipamento = equipamentoService.getById(dto.equipamentoId());


    Programacao programacao = programacaoMapper.toEntity(dto);

    programacao.setLote(lote);
    programacao.setEquipamento(equipamento);
    Programacao progSalva =    programacaoRepository.save(programacao);
    lote.setProgramacao(programacao);
    equipamento.adicionarProgramacao(programacao);

    loteService.atualizarEstoque(lote.getId(), dto.quantidadeConsumida());
    equipamentoService.atualizarProgramacaoEquipamento(equipamento.getId());




        return programacaoMapper.toDTO(progSalva);

    }


    public List<ProgramacaoResponseDTO> recuperarProgramacaoPorEquipamento(Long equipamentoId){

       List<Programacao>  programacao =  programacaoRepository.findAllProgramacaoByEquipamentoId(equipamentoId).orElseThrow(()-> new ProgramacaoNotFoundException("Não há nenhuma programa"));

      return  programacao.stream().map(programacaoMapper::toDTO).toList();
    }


}
