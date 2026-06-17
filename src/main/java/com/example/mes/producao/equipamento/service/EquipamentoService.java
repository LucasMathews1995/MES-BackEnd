package com.example.mes.producao.equipamento.service;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import com.example.mes.producao.equipamento.dto.EquipamentoOutputDTO;
import com.example.mes.producao.equipamento.dto.EquipamentoRequestDTO;
import com.example.mes.producao.equipamento.exceptions.EquipamentoNotValidException;
import com.example.mes.producao.equipamento.model.Equipamento;
import com.example.mes.producao.equipamento.repositories.EquipamentoRepository;
import com.example.mes.producao.rastreabilidade.domain.event.RastreabilidadeEquipamentoEvent;

import jakarta.transaction.Transactional;

@Service
public class EquipamentoService {

    private final EquipamentoRepository equipamentoRepository;
    private final ApplicationEventPublisher eventPublisher;

    public EquipamentoService(EquipamentoRepository equipamentoRepository, ApplicationEventPublisher eventPublisher) {
        this.equipamentoRepository = equipamentoRepository;
        this.eventPublisher = eventPublisher;

    }

    @Transactional
    public EquipamentoOutputDTO criarEquipamento(EquipamentoRequestDTO equipamento) {
        if (equipamentoRepository.checarSeNomeAchatadoExiste(equipamento.getNomeAchatado())) {
            throw new EquipamentoNotValidException("Equipamento com nome '" + equipamento.nome() + "' já existe.");
        }

        Equipamento equipamentoSalvo = new Equipamento(equipamento.nome(), equipamento.sigla(), equipamento.descricao(),
                equipamento.dataAtivacao(), equipamento.capacidade());

        equipamentoRepository.save(equipamentoSalvo);
        eventPublisher.publishEvent(new RastreabilidadeEquipamentoEvent(equipamentoSalvo));
        return new EquipamentoOutputDTO(equipamentoSalvo.getId(), equipamentoSalvo.getNome(),
                equipamentoSalvo.getSigla(), equipamentoSalvo.getDescricao());
    }

}
