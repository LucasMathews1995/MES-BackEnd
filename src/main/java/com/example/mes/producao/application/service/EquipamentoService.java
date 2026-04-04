package com.example.mes.producao.application.service;


import com.example.mes.producao.api.exception.AlreadyExistsEquipamentoException;
import com.example.mes.producao.api.exception.LoteAbastecidoException;
import com.example.mes.producao.api.exception.NotFoundEquipamentoException;
import com.example.mes.producao.application.dto.EquipamentoRequestDTO;
import com.example.mes.producao.application.mapper.EquipamentoMapper;
import com.example.mes.producao.domain.Equipamento;
import com.example.mes.producao.domain.Programacao;
import com.example.mes.producao.domain.StatusEquipamento;
import com.example.mes.producao.domain.StatusProgramacao;
import com.example.mes.producao.infraestructure.EquipamentoRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class EquipamentoService {

    private final EquipamentoRepository equipamentoRepository;
    private final EquipamentoMapper equipamentoMapper;
    private final ProgramacaoService programacaoService;


    //CREATE
    @Transactional
    public Equipamento criarEquipamento(EquipamentoRequestDTO dto) {

        if(equipamentoRepository.existsByNome(dto.nome())){
          throw new AlreadyExistsEquipamentoException("Já existe equipamento om esse nome" + dto.nome());
        }
        Equipamento equipamento = equipamentoMapper.toEntity(dto);
        equipamentoRepository.save(equipamento);
       return equipamento;
    }

    //GET
    public Equipamento buscarEquipamentoPorId(Long id) {
        return equipamentoRepository.findById(id).orElseThrow(()-> new NotFoundEquipamentoException("Nenhum equipamento encontrado."));

    }

    public List<Equipamento> buscarEquipamentos() {
        return equipamentoRepository.findAll();

    }
    //UPDATE
    @Transactional
    public void desativarEquipamento(Long id) {

        Equipamento equipamento = equipamentoRepository.findById(id).orElseThrow(() -> new NotFoundEquipamentoException("Nenhum equipamento encontrado."));


        equipamento.setStatusEquipamento(StatusEquipamento.PARADO);
        equipamento.getProgramacao().clear();

        equipamentoRepository.save(equipamento);
    }
    @Transactional
    public void pararEquipamento(Long id) {

        Equipamento equipamento = equipamentoRepository.findById(id).orElseThrow(() -> new NotFoundEquipamentoException("Nenhum equipamento encontrado."));


        equipamento.setStatusEquipamento(StatusEquipamento.PARADO);
        equipamento.setDataParado(LocalDateTime.now());


        equipamentoRepository.save(equipamento);
    }

    //DELETE
    @Transactional
    public void deletarEquipamento(Long id) {
        Equipamento equipamento = equipamentoRepository.findById(id).orElseThrow(() -> new NotFoundEquipamentoException("Nenhum equipamento encontrado."));

        List<Programacao> programasAbastecidos = programacaoService.buscarProgramacoesPorEquipamentoAndStatus(equipamento.getId(),StatusProgramacao.ABASTECIDO);




        if (!programasAbastecidos.isEmpty()) {
            throw new LoteAbastecidoException("Tem programa ainda como abastecido . Portanto não é possível deletar o Equipamento");
        }

        equipamentoRepository.delete(equipamento);

    }



}
