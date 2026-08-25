package com.example.mes.producao.programacao.application;



import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import com.example.mes.producao.equipamento.repositories.EquipamentoRepository;
import com.example.mes.producao.lote.domain.event.LoteEvent;
import com.example.mes.producao.lote.domain.exceptions.NotFoundLoteException;
import com.example.mes.producao.lote.domain.strategy.factory.EstrategiaCriacaoLoteFactory;
import com.example.mes.producao.lote.infraestructure.persistence.LoteRepository;
import com.example.mes.producao.ordemproducao.infraestructure.persistence.OrdemProducaoRepository;
import com.example.mes.producao.programacao.domain.Programacao;
import com.example.mes.producao.programacao.domain.StatusProgramacao;
import com.example.mes.producao.programacao.domain.strategy.normal.EstrategiaProgramacao;
import com.example.mes.producao.programacao.domain.strategy.normal.factory.EstrategiaProgramacaoFactory;
import com.example.mes.producao.programacao.infraestructure.dto.ProgramacaoOutputDTO;
import com.example.mes.producao.programacao.infraestructure.persistence.ProgramacaoRepository;
import com.example.mes.rastreabilidade.domain.event.RastreabilidadeProgramacaoEvent;

import jakarta.transaction.Transactional;

@Service
public class AlterarProgramacaoUseCase {

        private final ProgramacaoRepository programacaoRepository;
        private final EstrategiaProgramacaoFactory factory;
        private final ApplicationEventPublisher eventPublisher;


        public AlterarProgramacaoUseCase(ProgramacaoRepository programacaoRepository,
                        EquipamentoRepository equipamentoRepository,
                        LoteRepository loteRepository,
                        EstrategiaProgramacaoFactory factory, ApplicationEventPublisher eventPublisher,
                        OrdemProducaoRepository oProducaoRepository, EstrategiaCriacaoLoteFactory criacaoLoteFactory) {

                this.programacaoRepository = programacaoRepository;
                this.eventPublisher = eventPublisher;
                this.factory = factory;
                
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

               

                eventPublisher.publishEvent(new RastreabilidadeProgramacaoEvent(programacao));
                        
                
                return ProgramacaoOutputDTO.fromEntity(programacao);
        }

}
