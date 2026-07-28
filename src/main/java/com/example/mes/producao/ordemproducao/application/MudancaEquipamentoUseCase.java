package com.example.mes.producao.ordemproducao.application;

import org.springframework.stereotype.Service;

import com.example.mes.producao.equipamento.exceptions.NotFoundEquipamentoException;
import com.example.mes.producao.equipamento.model.Equipamento;
import com.example.mes.producao.equipamento.repositories.EquipamentoRepository;
import com.example.mes.producao.ordemproducao.domain.OrdemProducao;
import com.example.mes.producao.ordemproducao.dto.OrdemProducaoOuputDTO;
import com.example.mes.producao.ordemproducao.exceptions.OrdemProducaoNotFoundException;
import com.example.mes.producao.ordemproducao.infraestructure.persistence.OrdemProducaoRepository;

import jakarta.transaction.Transactional;
@Service
public class MudancaEquipamentoUseCase {


    private final OrdemProducaoRepository repository;
    private final EquipamentoRepository equipamentoRepository;

    public MudancaEquipamentoUseCase(OrdemProducaoRepository repository, EquipamentoRepository equipamentoRepository) {
        this.repository = repository;
        this.equipamentoRepository = equipamentoRepository;
    }


    
    @Transactional
    public OrdemProducaoOuputDTO mudarEquipamento(Long id, Long equipamentoId) {
        OrdemProducao op = repository.findById(id)
                .orElseThrow(() -> new OrdemProducaoNotFoundException("Ordem de produção não encontrada"));
     Equipamento equipamento = equipamentoRepository.findById(equipamentoId)
                .orElseThrow(() -> new NotFoundEquipamentoException("Equipamento não encontrado"));

       
        op.trocarEquipamento(equipamento);

        repository.save(op);

        return OrdemProducaoOuputDTO.fromEntity(op);
    }

}
