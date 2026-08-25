package com.example.mes;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.time.LocalDateTime;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import com.example.mes.producao.equipamento.model.Equipamento;
import com.example.mes.producao.equipamento.model.StatusEquipamento;
import com.example.mes.producao.lote.domain.Lote;
import com.example.mes.producao.lote.domain.StatusLote;
import com.example.mes.producao.ordemproducao.domain.OrdemProducao;
import com.example.mes.producao.ordemproducao.domain.StatusOP;
import com.example.mes.producao.programacao.domain.Programacao;
import com.example.mes.producao.programacao.domain.StatusProgramacao;
import com.example.mes.rastreabilidade.domain.Rastreabilidade;
import com.example.mes.rastreabilidade.domain.event.RastreabilidadeListener;
import com.example.mes.rastreabilidade.domain.event.RastreabilidadeProgramacaoEvent;
import com.example.mes.rastreabilidade.infraestructure.persistence.RastreabilidadeRepository;




public class RastreabilidadeListenerTest {

    @Mock
    private RastreabilidadeRepository rastreabilidadeRepository;

    
    @InjectMocks
    private RastreabilidadeListener rastreabilidadeListener;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }


    @Test
    @DisplayName("Deve rastrear a programacao apos o commit da transacao")
    void rastrearProgramacao( ){

        Equipamento equipamentoMock = new Equipamento();
        equipamentoMock.setId(1L);
        equipamentoMock.setNome("LTQ-01");
        equipamentoMock.setSigla("LTQ");
        equipamentoMock.setAtivo(true);
        equipamentoMock.setDataAtivacao(LocalDateTime.now());
        equipamentoMock.setDataParado(null );
        equipamentoMock.setStatusEquipamento(StatusEquipamento.OPERANDO);
        equipamentoMock.setCapacidade(1000L);
        
            OrdemProducao ordemProducaoMock = new OrdemProducao();
        ordemProducaoMock.trocarEquipamento(equipamentoMock);
        ordemProducaoMock.setNumeroOP("OP-123");
        ordemProducaoMock.setStatus(StatusOP.INICIADA);
        ordemProducaoMock.setDataCriacao(LocalDateTime.now());
        ordemProducaoMock.setDataEncerramento(null);
        

        Lote loteProduzidoMock = new Lote();
        loteProduzidoMock.setId(1L);
        loteProduzidoMock.setStatus(StatusLote.RESERVADO);
        loteProduzidoMock.setOrdemProducao(ordemProducaoMock);
        loteProduzidoMock.setQuantidadeDisponivel(100L);
        loteProduzidoMock.setDataCriacao(LocalDateTime.now());
        loteProduzidoMock.setDescricao("Lote de teste");


         Lote loteConsumidoMock = new Lote();
        loteConsumidoMock.setId(2L);
        loteConsumidoMock.setStatus(StatusLote.RESERVADO);
        loteConsumidoMock.setOrdemProducao(ordemProducaoMock);
        loteConsumidoMock.setQuantidadeDisponivel(100L);
        loteConsumidoMock.setDataCriacao(LocalDateTime.now());
        loteConsumidoMock.setDescricao("Lote de teste");

        Programacao programacaoMock = new Programacao();
        programacaoMock.setStatus(StatusProgramacao.CRIADA); 
        programacaoMock.setEquipamento(equipamentoMock);
        programacaoMock.setLoteProduzido(loteProduzidoMock);
        programacaoMock.setLoteConsumido(loteConsumidoMock);
        programacaoMock.setStatus(StatusProgramacao.CRIADA);


        RastreabilidadeProgramacaoEvent event = new RastreabilidadeProgramacaoEvent(programacaoMock);

        rastreabilidadeListener.rastrearProgramacao(event);


        verify(rastreabilidadeRepository, times(1)).save(any(Rastreabilidade.class));
    }





}
