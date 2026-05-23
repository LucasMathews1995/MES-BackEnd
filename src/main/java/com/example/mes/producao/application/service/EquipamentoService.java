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
import com.example.mes.producao.infraestructure.ProgramacaoRepository;

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
    private final ProgramacaoRepository programacaoRepository;

    @Transactional
    public Equipamento criarEquipamento(EquipamentoRequestDTO dto) {
        String limpo = dto.nome().trim().toLowerCase();
        if (equipamentoRepository.existsByNomeIgnoreCase(limpo)) {
            throw new AlreadyExistsEquipamentoException("Já existe equipamento om esse nome" + dto.nome());
        }
        Equipamento equipamento = equipamentoMapper.toEntityCriarEquipamento(dto);
        equipamentoRepository.save(equipamento);
        return equipamento;
    }

    public Equipamento buscarEquipamentoPorId(Long id) {
        return equipamentoRepository.findById(id)
                .orElseThrow(() -> new NotFoundEquipamentoException("Nenhum equipamento encontrado."));

    }

    public List<Equipamento> buscarEquipamentos() {
        List<Equipamento> equipamentos = equipamentoRepository.findAll();

        if (equipamentos.isEmpty()) {
            throw new NotFoundEquipamentoException("Nenhum equipamento encontrado.");
        }
        return equipamentos;

    }

    @Transactional
    public void desativarEquipamento(Long id) {

        Equipamento equipamento = equipamentoRepository.findById(id)
                .orElseThrow(() -> new NotFoundEquipamentoException("Nenhum equipamento encontrado."));

        equipamento.desativarEquipamento();

        equipamentoRepository.save(equipamento);
    }

    @Transactional
    public void ativarEquipamento(Long id) {

        Equipamento equipamento = equipamentoRepository.findById(id)
                .orElseThrow(() -> new NotFoundEquipamentoException("Nenhum equipamento encontrado."));
        equipamento.ativarEquipamento();

        equipamentoRepository.save(equipamento);

    }

    @Transactional
    public void deletarEquipamento(Long id) {
        Equipamento equipamento = equipamentoRepository.findById(id)
                .orElseThrow(() -> new NotFoundEquipamentoException("Nenhum equipamento encontrado."));

        if (equipamento.getStatusEquipamento() != StatusEquipamento.PARADO) {
            throw new NotFoundEquipamentoException("Equipamento não está parado:" + id);
        }

        equipamentoRepository.delete(equipamento);

    }

}
