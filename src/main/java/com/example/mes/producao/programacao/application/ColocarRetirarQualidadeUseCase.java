package com.example.mes.producao.programacao.application;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import com.example.mes.producao.equipamento.model.Equipamento;
import com.example.mes.producao.equipamento.repositories.EquipamentoRepository;
import com.example.mes.producao.lote.domain.event.LoteEvent;
import com.example.mes.producao.lote.domain.strategy.factory.EstrategiaCriacaoLoteFactory;
import com.example.mes.producao.programacao.domain.Programacao;
import com.example.mes.producao.programacao.domain.StatusProgramacao;
import com.example.mes.producao.programacao.domain.exceptions.NotFoundProgramacaoException;
import com.example.mes.producao.programacao.domain.strategy.normal.factory.EstrategiaProgramacaoFactory;
import com.example.mes.producao.programacao.domain.strategy.qualidade.EstrategiaColocarQualidade;
import com.example.mes.producao.programacao.domain.strategy.qualidade.factory.EstrategiaQualidadeFactory;
import com.example.mes.producao.programacao.infraestructure.dto.ProgramacaoInputDTO;
import com.example.mes.producao.programacao.infraestructure.dto.ProgramacaoOutputDTO;
import com.example.mes.producao.programacao.infraestructure.persistence.ProgramacaoRepository;
import com.example.mes.rastreabilidade.domain.event.RastreabilidadeProgramacaoEvent;

@Service
public class ColocarRetirarQualidadeUseCase {

    private final ProgramacaoRepository programacaoRepository;
    private final EstrategiaQualidadeFactory factory;
    private final ApplicationEventPublisher eventPublisher;
    private final EquipamentoRepository equipamentoRepository;

    public ColocarRetirarQualidadeUseCase(ProgramacaoRepository programacaoRepository,
            EstrategiaQualidadeFactory factory, ApplicationEventPublisher eventPublisher,
            EquipamentoRepository equipamentoRepository) {

        this.programacaoRepository = programacaoRepository;
        this.eventPublisher = eventPublisher;
        this.factory = factory;
        this.equipamentoRepository = equipamentoRepository;

    }

    public ProgramacaoOutputDTO alterarStatusQualidade(Long programacaoId, StatusProgramacao novoStatus) {
       
        // jogar direto a estrategia de qualidade e processar a programacao
        Programacao programacao = programacaoRepository.findById(programacaoId)
                .orElseThrow(() -> new NotFoundProgramacaoException(
                        "Programação não encontrada com id: " + programacaoId));

        var estrategia = factory.obterEstrategia(novoStatus);
        estrategia.processar(programacao);
        
        eventPublisher.publishEvent(new LoteEvent(programacao.getStatus(), programacao));
        eventPublisher.publishEvent(new RastreabilidadeProgramacaoEvent(programacao));


        return  ProgramacaoOutputDTO.fromEntity(programacao);
    }



}
