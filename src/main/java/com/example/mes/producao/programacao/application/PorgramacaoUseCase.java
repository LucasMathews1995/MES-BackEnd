package com.example.mes.producao.programacao.application;


import java.util.List;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import com.example.mes.producao.equipamento.exceptions.NotFoundEquipamentoException;
import com.example.mes.producao.equipamento.model.Equipamento;
import com.example.mes.producao.equipamento.repositories.EquipamentoRepository;
import com.example.mes.producao.lote.domain.Lote;
import com.example.mes.producao.lote.domain.event.LoteEvent;
import com.example.mes.producao.lote.domain.exceptions.NotFoundLoteException;
import com.example.mes.producao.lote.domain.strategy.EstrategiaCriacaoLote;
import com.example.mes.producao.lote.domain.strategy.factory.EstrategiaCriacaoLoteFactory;
import com.example.mes.producao.lote.infraestructure.persistence.LoteRepository;
import com.example.mes.producao.ordemproducao.domain.OrdemProducao;
import com.example.mes.producao.ordemproducao.exceptions.OPNotFoundException;
import com.example.mes.producao.ordemproducao.infraestructure.persistence.OrdemProducaoRepository;
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
        private final EstrategiaCriacaoLoteFactory criacaoLoteFactory;
        private final ApplicationEventPublisher eventPublisher;
        private final OrdemProducaoRepository oProducaoRepository;

        public PorgramacaoUseCase(ProgramacaoRepository programacaoRepository,
                        EquipamentoRepository equipamentoRepository,
                        LoteRepository loteRepository,
                        EstrategiaProgramacaoFactory factory, ApplicationEventPublisher eventPublisher,
                        OrdemProducaoRepository oProducaoRepository, EstrategiaCriacaoLoteFactory criacaoLoteFactory) {
                this.programacaoRepository = programacaoRepository;
                this.equipamentoRepository = equipamentoRepository;
                this.loteRepository = loteRepository;
                this.eventPublisher = eventPublisher;
                this.factory = factory;
                this.oProducaoRepository = oProducaoRepository;
                this.criacaoLoteFactory = criacaoLoteFactory;
        }

        @Transactional
        public void criarProgramacao(ProgramacaoInputDTO input) {

                Equipamento equipamento = equipamentoRepository.findById(input.equipamentoId())
                                .orElseThrow(() -> new NotFoundEquipamentoException(
                                                "Equipamento não encontrado com id: " + input.equipamentoId()));

                Lote lote = loteRepository.findById(input.loteId())
                                .orElseThrow(() -> new NotFoundLoteException(
                                                "Lote não encontrado com id: " + input.loteId()));

                OrdemProducao ordemProducao = oProducaoRepository.findById(lote.getOrdemProducao().getId())
                                .orElseThrow(() -> new OPNotFoundException(
                                                "nenhuma OP achada com esse id : " + lote.getOrdemProducao().getId()));

                EstrategiaCriacaoLote estrategiaCriacaoLote = criacaoLoteFactory.obEstrategiaCriacaoLote(equipamento,
                                input.quantidadeConsumida());

                

                List<Lote> lotesProcessados = estrategiaCriacaoLote.executar(ordemProducao, lote,
                                                                input.quantidadeConsumida(),
                                                                equipamento.getCapacidade());
                                             

                loteRepository.saveAll(lotesProcessados);

                List<Programacao> programacoes = lotesProcessados.stream()
                                .map(l -> Programacao.criarPrograma(ordemProducao, equipamento, lote, l,
                                                input.quantidadeConsumida()))
                                .toList();

                programacaoRepository.saveAll(programacoes);

                programacoes.forEach(p -> eventPublisher.publishEvent(new RastreabilidadeEvent(p)));

        }

        @Transactional
        public ProgramacaoOutputDTO alterarStatus(Long programacaoId, StatusProgramacao novoStatus) {
                Programacao programacao = programacaoRepository.findById(programacaoId)
                                .orElseThrow(() -> new NotFoundLoteException(
                                                "Programação não encontrada com id: " + programacaoId));



                EstrategiaProgramacao estrategia = factory.obterEstrategia(novoStatus);
                estrategia.processar(programacao);

                eventPublisher
                                .publishEvent(new LoteEvent(novoStatus, programacao));

               

                eventPublisher.publishEvent(new RastreabilidadeEvent(programacao));
                        
                
                return ProgramacaoOutputDTO.fromEntity(programacao);
        }

}
