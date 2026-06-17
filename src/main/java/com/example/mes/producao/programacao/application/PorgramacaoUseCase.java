package com.example.mes.producao.programacao.application;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import com.example.mes.producao.equipamento.exceptions.NotFoundEquipamentoException;
import com.example.mes.producao.equipamento.model.Equipamento;
import com.example.mes.producao.equipamento.repositories.EquipamentoRepository;
import com.example.mes.producao.lote.domain.Lote;
import com.example.mes.producao.lote.domain.event.LoteEvent;
import com.example.mes.producao.lote.domain.exceptions.NotFoundLoteException;
import com.example.mes.producao.lote.infraestructure.persistence.LoteRepository;
import com.example.mes.producao.programacao.domain.Programacao;
import com.example.mes.producao.programacao.domain.StatusProgramacao;
import com.example.mes.producao.programacao.domain.strategy.EstrategiaProgramacao;
import com.example.mes.producao.programacao.domain.strategy.factory.EstrategiaProgramacaoFactory;
import com.example.mes.producao.programacao.infraestructure.dto.ProgramacaoOutputDTO;
import com.example.mes.producao.programacao.infraestructure.dto.ProgramacaoInputDTO;
import com.example.mes.producao.programacao.infraestructure.persistence.ProgramacaoRepository;
import com.example.mes.producao.rastreabilidade.domain.event.RastreabilidadeEvent;


import jakarta.transaction.Transactional;

@Service
public class PorgramacaoUseCase {

    private final ProgramacaoRepository programacaoRepository;
    private final EquipamentoRepository equipamentoRepository;
    private final LoteRepository loteRepository;
    private final EstrategiaProgramacaoFactory factory;
    private final ApplicationEventPublisher eventPublisher;

    public PorgramacaoUseCase(ProgramacaoRepository programacaoRepository, EquipamentoRepository equipamentoRepository,
            LoteRepository loteRepository,
            EstrategiaProgramacaoFactory factory, ApplicationEventPublisher eventPublisher) {
        this.programacaoRepository = programacaoRepository;
        this.equipamentoRepository = equipamentoRepository;
        this.loteRepository = loteRepository;
        this.eventPublisher = eventPublisher;
        this.factory = factory;
    }

    @Transactional
    public ProgramacaoOutputDTO criarProgramacao(ProgramacaoInputDTO input) {

        Equipamento equipamento = equipamentoRepository.findById(input.equipamentoId())
                .orElseThrow(() -> new NotFoundEquipamentoException(
                        "Equipamento não encontrado com id: " + input.equipamentoId()));

        Lote lote = loteRepository.findById(input.loteId())
                .orElseThrow(() -> new NotFoundLoteException("Lote não encontrado com id: " + input.loteId()));

        


        Programacao programacao = Programacao.criarPrograma(equipamento, lote, input.quantidadeConsumida());

        programacaoRepository.save(programacao);

        eventPublisher.publishEvent(
                new RastreabilidadeEvent(programacao));

        return ProgramacaoOutputDTO.fromEntity(programacao);
    }

    @Transactional
    public ProgramacaoOutputDTO alterarStatus(Long programacaoId, StatusProgramacao novoStatus) {
        Programacao programacao = programacaoRepository.findById(programacaoId)
                .orElseThrow(() -> new NotFoundLoteException("Programação não encontrada com id: " + programacaoId));


        Long equiapemtnoId = programacao.getEquipamento().getId();
        
        Integer ultimaFila = programacaoRepository.findMaxOrdemByEquipamentoAndStatus(
       equiapemtnoId, 
        novoStatus
    );


        EstrategiaProgramacao estrategia = factory.obterEstrategia(novoStatus);
        estrategia.processar(programacao,ultimaFila);

        eventPublisher
                .publishEvent(new LoteEvent(novoStatus, programacao ));

        programacaoRepository.save(programacao);

        eventPublisher.publishEvent(new RastreabilidadeEvent(programacao));

        return ProgramacaoOutputDTO.fromEntity(programacao);
    }
}
