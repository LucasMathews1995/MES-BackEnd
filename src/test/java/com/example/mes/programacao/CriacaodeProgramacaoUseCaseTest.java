package com.example.mes.programacao;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.context.ApplicationEventPublisher;

import com.example.mes.producao.equipamento.exceptions.NotFoundEquipamentoException;
import com.example.mes.producao.equipamento.model.Equipamento;
import com.example.mes.producao.equipamento.repositories.EquipamentoRepository;
import com.example.mes.producao.lote.domain.Lote;
import com.example.mes.producao.lote.domain.exceptions.LoteAbastecidoException;
import com.example.mes.producao.lote.domain.exceptions.NotFoundLoteException;
import com.example.mes.producao.lote.domain.strategy.criacaoLoteStrategy.EstrategiaCriacaoLote;
import com.example.mes.producao.lote.domain.strategy.factory.EstrategiaCriacaoLoteFactory;
import com.example.mes.producao.lote.infraestructure.persistence.LoteRepository;
import com.example.mes.producao.ordemproducao.domain.OrdemProducao;
import com.example.mes.producao.ordemproducao.domain.StatusOP;
import com.example.mes.producao.ordemproducao.infraestructure.persistence.OrdemProducaoRepository;
import com.example.mes.producao.programacao.application.CriacaodeProgramacaoUseCase;
import com.example.mes.producao.programacao.domain.Programacao;
import com.example.mes.producao.programacao.infraestructure.dto.ProgramacaoInputDTO;
import com.example.mes.producao.programacao.infraestructure.persistence.ProgramacaoRepository;
import com.example.mes.rastreabilidade.domain.event.RastreabilidadeProgramacaoEvent;

class CriacaodeProgramacaoUseCaseTest {

    @Mock
    private ProgramacaoRepository programacaoRepository;
    @Mock
    private EquipamentoRepository equipamentoRepository;
    @Mock
    private LoteRepository loteRepository;
    @Mock
    private OrdemProducaoRepository oProducaoRepository;
    @Mock
    private EstrategiaCriacaoLoteFactory criacaoLoteFactory;
    @Mock
    private ApplicationEventPublisher eventPublisher;
    @Mock
    private EstrategiaCriacaoLote estrategiaMock;

    @InjectMocks
    private CriacaodeProgramacaoUseCase useCase;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Deve criar a programação com sucesso e salvar os lotes processados")
    void deveriaCriarProgramacaoComSucesso() {


        // 1. Arrange (Preparação)
        Long equipamentoId = 1L;
        Long loteId = 2L;
        Long ordemProducaoId = 3L;
        Long quantidadeLote = 50L;
        Long qtdeConsumida = 100L;
        

        // Criando DTO de Input (supondo que seja um Record)
        ProgramacaoInputDTO input = new ProgramacaoInputDTO(loteId, equipamentoId, qtdeConsumida);

        // Mockando Entidades
        Equipamento equipamentoMock = mock(Equipamento.class);
        when(equipamentoMock.getCapacidade()).thenReturn(500L);
        when(equipamentoMock.isAtivo()).thenReturn(true);
        when(equipamentoMock.getId()).thenReturn(equipamentoId);

        OrdemProducao ordemProducaoMock = mock(OrdemProducao.class);
        when(ordemProducaoMock.getId()).thenReturn(ordemProducaoId);
        when(ordemProducaoMock.getEquipamentoId()).thenReturn(equipamentoId);
        when(ordemProducaoMock.getStatus()).thenReturn(StatusOP.PROCESSANDO);
 
        Lote loteMock = mock(Lote.class);
        when(loteMock.getOrdemProducao()).thenReturn(ordemProducaoMock);
        when(loteMock.getQuantidadeDisponivel()).thenReturn(quantidadeLote);

        Lote loteProcessadoMock = mock(Lote.class);
        List<Lote> lotesProcessados = List.of(loteProcessadoMock);

        // Configurando os retornos dos Repositórios e Factory
        when(equipamentoRepository.findById(anyLong())).thenReturn(Optional.of(equipamentoMock));

        when(loteRepository.findById(anyLong())).thenReturn(Optional.of(loteMock));
      
        when(oProducaoRepository.findById(anyLong())).thenReturn(Optional.of(ordemProducaoMock));
        
        when(criacaoLoteFactory.obEstrategiaCriacaoLote(equipamentoMock, qtdeConsumida))
                .thenReturn(estrategiaMock);
                
        when(estrategiaMock.executar(ordemProducaoMock, loteMock, qtdeConsumida, equipamentoMock.getCapacidade()))
                .thenReturn(lotesProcessados);

        // 2. Act (Ação)
        useCase.criarProgramacao(input);

        // 3. Assert (Verificações)
        // Garante que as buscas foram feitas
        verify(equipamentoRepository).findById(equipamentoId);
        verify(loteRepository).findById(loteId);
        verify(oProducaoRepository).findById(ordemProducaoId);

        // Garante que a Strategy foi criada e executada
        verify(criacaoLoteFactory).obEstrategiaCriacaoLote(equipamentoMock, qtdeConsumida);
        verify(estrategiaMock).executar(ordemProducaoMock, loteMock, qtdeConsumida, equipamentoMock.getCapacidade());
        

        // Garante que os lotes processados foram salvos
        verify(loteRepository).saveAll(lotesProcessados);

        // Usamos um Captor para capturar a lista de programações que foi enviada para o saveAll
        @SuppressWarnings("unchecked")
        ArgumentCaptor<List<Programacao>> programacoesCaptor = ArgumentCaptor.forClass(List.class);
        verify(programacaoRepository).saveAll(programacoesCaptor.capture());
        
        List<Programacao> programacoesSalvas = programacoesCaptor.getValue();
        assertFalse(programacoesSalvas.isEmpty());
        assertEquals(1, programacoesSalvas.size()); // Porque retornamos 1 lote processado no mock

        // Garante que o evento foi disparado
        verify(eventPublisher, times(1)).publishEvent(any(RastreabilidadeProgramacaoEvent.class));
    }

    @Test
    @DisplayName("Deve lançar exceção quando o Equipamento não for encontrado")
    void deveriaLancarExcecaoQuandoEquipamentoNaoExistir() {
        // Arrange
        ProgramacaoInputDTO input = new ProgramacaoInputDTO(99L, 2L, 100L);
        
        when(equipamentoRepository.findById(99L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(NotFoundEquipamentoException.class, () -> {
            useCase.criarProgramacao(input);
        });

        // Garante que não chamou mais nada depois do erro
        verify(loteRepository, never()).findById(anyLong());
        verify(criacaoLoteFactory, never()).obEstrategiaCriacaoLote(any(), any());
    }

    @Test
    @DisplayName("Deve lançar exceção quando o Lote não for encontrado")
    void deveriaLancarExcecaoQuandoLoteNaoExistir() {
        // Arrange
        ProgramacaoInputDTO input = new ProgramacaoInputDTO(1L, 99L, 100L);
        Equipamento equipamentoMock = mock(Equipamento.class);
        
        when(equipamentoRepository.findById(1L)).thenReturn(Optional.of(equipamentoMock));
        when(loteRepository.findById(99L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(NotFoundLoteException.class, () -> {
            useCase.criarProgramacao(input);
        });

        // Garante que parou a execução aqui
        verify(oProducaoRepository, never()).findById(anyLong());
        verify(criacaoLoteFactory, never()).obEstrategiaCriacaoLote(any(), any());
    }


    @Test
@DisplayName("Deve lançar exceção quando o saldo do lote for insuficiente")
void deveriaLancarExcecaoQuandoSaldoForInsuficiente() {
    // Arrange ... (seus mocks)
    Long quantidadeLote = 500L;

    OrdemProducao ordemProducaoMock = mock(OrdemProducao.class);
        when(ordemProducaoMock.getId()).thenReturn(1L);
        when(ordemProducaoMock.getEquipamentoId()).thenReturn(1L);
        when(ordemProducaoMock.getStatus()).thenReturn(StatusOP.PROCESSANDO);

    Lote loteMock = mock(Lote.class);
    when(loteMock.getQuantidadeDisponivel()).thenReturn(quantidadeLote);
    when(loteMock.getOrdemProducao()).thenReturn(ordemProducaoMock);

    Equipamento equipamentoMock = mock(Equipamento.class);
    when(equipamentoMock.getCapacidade()).thenReturn(1000L);
    when(equipamentoMock.isAtivo()).thenReturn(true);
    when(equipamentoMock.getId()).thenReturn(1L);

    when(loteRepository.findById(anyLong())).thenReturn(Optional.of(loteMock));

    ProgramacaoInputDTO input = new ProgramacaoInputDTO(1L, 1L, 1000L); 

    when(equipamentoRepository.findById(anyLong())).thenReturn(Optional.of(equipamentoMock));
    when(oProducaoRepository.findById(anyLong())).thenReturn(Optional.of(ordemProducaoMock));
    when(criacaoLoteFactory.obEstrategiaCriacaoLote(equipamentoMock, input.quantidadeConsumida()))
    .thenReturn(estrategiaMock);

    

    // Configura para lançar a exceção onde o código de produção realmente chama
   
            doThrow(new LoteAbastecidoException("Saldo insuficiente"))
    .when(estrategiaMock)
    .executar(any(), any(), any(), any());

    // Act & Assert: Garante que a exceção foi disparada ao executar o UseCase
    assertThrows(LoteAbastecidoException.class, () -> {
        useCase.criarProgramacao(input);
    });


}}
