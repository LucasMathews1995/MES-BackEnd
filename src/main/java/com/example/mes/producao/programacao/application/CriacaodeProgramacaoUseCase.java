package com.example.mes.producao.programacao.application;

import java.util.List;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import com.example.mes.producao.equipamento.exceptions.NotFoundEquipamentoException;
import com.example.mes.producao.equipamento.model.Equipamento;
import com.example.mes.producao.equipamento.repositories.EquipamentoRepository;
import com.example.mes.producao.lote.domain.Lote;
import com.example.mes.producao.lote.domain.exceptions.NotFoundLoteException;
import com.example.mes.producao.lote.domain.strategy.criacaoLoteStrategy.EstrategiaCriacaoLote;
import com.example.mes.producao.lote.domain.strategy.factory.EstrategiaCriacaoLoteFactory;
import com.example.mes.producao.lote.infraestructure.persistence.LoteRepository;
import com.example.mes.producao.ordemproducao.domain.OrdemProducao;
import com.example.mes.producao.ordemproducao.exceptions.OrdemProducaoNotFoundException;
import com.example.mes.producao.ordemproducao.infraestructure.persistence.OrdemProducaoRepository;
import com.example.mes.producao.programacao.domain.Programacao;
import com.example.mes.producao.programacao.infraestructure.dto.ProgramacaoInputDTO;
import com.example.mes.producao.programacao.infraestructure.dto.ProgramacaoOutputDTO;
import com.example.mes.producao.programacao.infraestructure.persistence.ProgramacaoRepository;
import com.example.mes.rastreabilidade.domain.event.RastreabilidadeProgramacaoEvent;

import jakarta.transaction.Transactional;

@Service
public class CriacaodeProgramacaoUseCase {

    private final ProgramacaoRepository programacaoRepository;
    private final EquipamentoRepository equipamentoRepository;
    private final LoteRepository loteRepository;
    private final OrdemProducaoRepository oProducaoRepository;
    private final ApplicationEventPublisher eventPublisher;
    private final EstrategiaCriacaoLoteFactory criacaoLoteFactory;

    public CriacaodeProgramacaoUseCase(ProgramacaoRepository programacaoRepository,
            EquipamentoRepository equipamentoRepository,
            LoteRepository loteRepository,
            OrdemProducaoRepository oProducaoRepository,
            EstrategiaCriacaoLoteFactory criacaoLoteFactory, ApplicationEventPublisher eventPublisher) {
        this.programacaoRepository = programacaoRepository;
        this.loteRepository = loteRepository;
        this.equipamentoRepository = equipamentoRepository;
        this.oProducaoRepository = oProducaoRepository;
        this.eventPublisher = eventPublisher;
        this.criacaoLoteFactory = criacaoLoteFactory;

    }

    @Transactional
    public List<ProgramacaoOutputDTO> criarProgramacao(ProgramacaoInputDTO input) {

        Equipamento equipamento = equipamentoRepository.findById(input.equipamentoId())
                .orElseThrow(() -> new NotFoundEquipamentoException(
                        "Equipamento não encontrado com id: " + input.equipamentoId()));

        Lote lote = loteRepository.findById(input.loteId())
                .orElseThrow(() -> new NotFoundLoteException(
                        "Lote não encontrado com id: " + input.loteId()));

       


        OrdemProducao ordemProducao = oProducaoRepository.findById(lote.getOrdemProducao().getId())
                .orElseThrow(() -> new OrdemProducaoNotFoundException(
                        "nenhuma OP achada com esse id : " + lote.getOrdemProducao().getId()));

        EstrategiaCriacaoLote estrategiaCriacaoLote = criacaoLoteFactory.obEstrategiaCriacaoLote(equipamento,
                input.quantidadeConsumida());

        List<Lote> lotesProcessados = estrategiaCriacaoLote.executar(ordemProducao, lote,
                input.quantidadeConsumida(),
                equipamento.getCapacidade());

        loteRepository.saveAll(lotesProcessados);

        List<Programacao> programacoes = lotesProcessados.stream()
                .map(l -> Programacao.criarPrograma(ordemProducao, equipamento, lote, l ))
                .toList();

        programacaoRepository.saveAll(programacoes);

        programacoes.forEach(p -> eventPublisher.publishEvent(new RastreabilidadeProgramacaoEvent(p)));
         return programacoes.stream().map(ProgramacaoOutputDTO::fromEntity).toList();               
    }
}
