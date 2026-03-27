package com.example.mes.producao.application.service;


import com.example.mes.producao.api.exception.AlreadyExistsEquipamentoException;
import com.example.mes.producao.api.exception.NotFoundEquipamentoException;
import com.example.mes.producao.api.exception.ProgramacaoNotFoundException;
import com.example.mes.producao.application.dto.EquipamentoRequestDTO;
import com.example.mes.producao.application.dto.EquipamentoResponseDTO;
import com.example.mes.producao.application.mapper.EquipamentoMapper;
import com.example.mes.producao.domain.Equipamento;
import com.example.mes.producao.domain.StatusEquipamento;
import com.example.mes.producao.infraestructure.EquipamentoRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class EquipamentoService {

    private final EquipamentoRepository equipamentoRepository;
    private final EquipamentoMapper equipamentoMapper;


    //CREATE
    @Transactional
    public EquipamentoResponseDTO criarEquipamento(EquipamentoRequestDTO dto) {

        if(equipamentoRepository.existsByNome(dto.nome())){
          throw new AlreadyExistsEquipamentoException("Já existe equipamento om esse nome" + dto.nome());
        }
        Equipamento equipamento = equipamentoMapper.toEntity(dto);
        equipamentoRepository.save(equipamento);
       return equipamentoMapper.toDTO(equipamento);
    }

    //GET
    public EquipamentoResponseDTO buscarEquipamento(Long id) {
        Equipamento equipamento =equipamentoRepository.findById(id).orElseThrow(()-> new NotFoundEquipamentoException("Nenhum equipamento encontrado."));
        return equipamentoMapper.toDTO(equipamento);
    }

    public List<EquipamentoResponseDTO> buscarEquipamentos() {
        List<Equipamento> equipamentos =equipamentoRepository.findAll();

        return equipamentos.stream().sorted(Comparator.comparing(Equipamento::getNome)).map(equipamentoMapper::toDTO).toList();


    }
    //UPDATE
    @Transactional
    public void desactivateEquipment(Long id , StatusEquipamento status) {

        Equipamento equipamento =equipamentoRepository.findById(id).orElseThrow(()-> new NotFoundEquipamentoException("Nenhum equipamento encontrado."));
        equipamento.setStatusEquipamento(status);
        equipamentoRepository.save(equipamento);

    }

    //DELETE
    @Transactional
    public void deleteEquipment(Long id) {
        Equipamento equipamento =equipamentoRepository.findById(id).orElseThrow(()-> new NotFoundEquipamentoException("Nenhum equipamento encontrado."));
        if(!equipamento.getProgramacao().isEmpty()){throw new ProgramacaoNotFoundException("Nenhuma programacao encontrada.");}
        equipamentoRepository.delete(equipamento);

    }



}
