package com.example.mes.producao.application.service;


import com.example.mes.producao.api.exception.AlreadyExistsEquipamentoException;
import com.example.mes.producao.api.exception.NotFoundEquipamentoException;
import com.example.mes.producao.application.dto.EquipamentoRequestDTO;
import com.example.mes.producao.application.dto.EquipamentoResponseDTO;
import com.example.mes.producao.application.mapper.EquipamentoMapper;
import com.example.mes.producao.domain.Equipamento;
import com.example.mes.producao.infraestructure.EquipamentoRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EquipamentoService {

    private final EquipamentoRepository equipamentoRepository;
    private final EquipamentoMapper equipamentoMapper;


    @Transactional
    public EquipamentoResponseDTO salvar(EquipamentoRequestDTO dto){

        if (equipamentoRepository.existsByNome((dto.nome()))){
            throw new AlreadyExistsEquipamentoException("Já existe um equipamento com este nome.");
        }




        Equipamento equipamento =     equipamentoRepository.save(equipamentoMapper.toEntity(dto));

        return equipamentoMapper.toDTO(equipamento);


    }

    public List<EquipamentoResponseDTO> getAll(){
        List<Equipamento> equipamentos = equipamentoRepository.findAll();
        if(equipamentos.isEmpty()){
            throw new NotFoundEquipamentoException("Nenhum equipamento na lista.");
        }
        return equipamentos.stream().map(equipamentoMapper::toDTO).toList();

    }

    public Equipamento getById (Long id ){
        return equipamentoRepository.findById(id).orElseThrow(()-> new  NotFoundEquipamentoException("Nenhum equipamento na lista."));

    }


}
