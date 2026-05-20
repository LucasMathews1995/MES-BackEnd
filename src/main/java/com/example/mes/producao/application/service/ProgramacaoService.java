package com.example.mes.producao.application.service;

import com.example.mes.producao.api.exception.NotFoundEquipamentoException;
import com.example.mes.producao.api.exception.NotProgramacaoValidException;
import com.example.mes.producao.api.exception.ProgramacaoNotFoundException;
import com.example.mes.producao.application.dto.ProgramacaoOrdemProducaoDTO;
import com.example.mes.producao.application.dto.ProgramacaoResponseDTO;
import com.example.mes.producao.application.mapper.ProgramacaoMapper;
import com.example.mes.producao.domain.Equipamento;
import com.example.mes.producao.domain.Lote;
import com.example.mes.producao.domain.OrdemProducao;
import com.example.mes.producao.domain.Programacao;
import com.example.mes.producao.domain.StatusEquipamento;
import com.example.mes.producao.domain.StatusProgramacao;
import com.example.mes.producao.infraestructure.EquipamentoRepository;
import com.example.mes.producao.infraestructure.ProgramacaoRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.ObjectInputFilter.Status;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Service
@AllArgsConstructor
public class ProgramacaoService {

    private final ProgramacaoRepository programacaoRepository;
    private final EquipamentoRepository equipamentoRepository;
    private final ProgramacaoMapper programacaoMapper;

    @Transactional
    public void salvarProgramacao(Programacao programacao) {

        programacaoRepository.save(programacao);
    }

    @Transactional
    public Programacao criarPrograma(Programacao programacao, Lote lote, Equipamento equipamento,
            Integer quantidadeConsumida) {

        if (existirProgamaPorLoteId(lote.getId())) {
            throw new NotProgramacaoValidException("Esse lote  já possui programa");

        }
        if (equipamento.getStatusEquipamento() == StatusEquipamento.PARADO) {
            throw new NotProgramacaoValidException("Não é possível programar: O equipamento está parado.");
        }

        if (lote.getOrdemProducao() == null) {
            throw new NotProgramacaoValidException(
                    "O lote " + lote.getNome() + " não possui ordem de produção, portanto não pode ser programado");

        }

        Integer ultimaFila = buscarMaxFilaDoEquipamento(equipamento.getId());
        int proximaFila = (ultimaFila != null ? ultimaFila : 0) + 1;

        programacao.setFila(proximaFila);

        lote.consumirQuantidade(quantidadeConsumida);

        lote.adicionarProgramacao(programacao);

        return programacao;
    }

    public Programacao buscarProgramacaoPorId(Long id) {
        return programacaoRepository.findById(id)
                .orElseThrow(() -> new ProgramacaoNotFoundException("Nenhuma programacao encontrada"));
    }

    public List<ProgramacaoOrdemProducaoDTO> buscarProgramacoesPorEquipamentoIdEStatus(Long id,
            StatusProgramacao status) {
        List<ProgramacaoOrdemProducaoDTO> programacao = programacaoRepository
                .findProgramacoesPorEquipamentoEStatusOrdem(id, status);

        if (programacao.isEmpty()) {
            return Collections.emptyList();
        }

        return programacao;
    }

    public boolean validarEquipamentoAndStatus(Long equipamentoId ) {
      
      return  programacaoRepository.existsByEquipamentoIdAndStatus(equipamentoId, StatusProgramacao.ABASTECIDO);
         
     
    }

   public List<Programacao> buscarProgramacoesPorEquipamentoAndStatus(Long equipamentoId , StatusProgramacao status) {
        List<Programacao> programacao = programacaoRepository
                .findByEquipamentoIdAndStatus(equipamentoId, status);

        if (programacao.isEmpty()) {
            return Collections.emptyList();
        }

        return programacao;
    }

    @Transactional
    public Programacao retirarQualidade(Equipamento equipamento, OrdemProducao ordem, Long id) {

        Programacao programacao = buscarProgramacaoPorId(id);

        Lote lote = programacao.getLote();

        lote.retirarDeQualidade(ordem);
        lote.programarLote();
        programacao.setEquipamento(equipamento);

        Integer ultimaFila = buscarMaxFilaDoEquipamento(equipamento.getId());
        int proximaFila = (ultimaFila != null ? ultimaFila : 0) + 1;

        programacao.setFila(proximaFila);
        return programacaoRepository.save(programacao);
        

    }

    public List<ProgramacaoResponseDTO> buscarProgramacoesPorEquipamentoAteProduzido(Long equipamentoId) {

        List<StatusProgramacao> statusIgnorados = Arrays.asList(
                StatusProgramacao.PRODUZIDO,
                StatusProgramacao.APROVADO,
                StatusProgramacao.QUALIDADE);

        if (!equipamentoRepository.existsById(equipamentoId)) {
            throw new NotFoundEquipamentoException("Nenhuma equipamento encontrada");
        }

        List<Programacao> programacoes = programacaoRepository.findByEquipamentoIdAndStatusNotIn(equipamentoId,
                statusIgnorados);

        return programacoes.stream().map(it -> programacaoMapper.toDTODetalhe(it, it.getLote(), it.getEquipamento()))
                .toList();

    }

    public List<ProgramacaoOrdemProducaoDTO> buscarProgrmacaoDoEquipamento(Long equipamentoId) {

        List<ProgramacaoOrdemProducaoDTO> programacoes = programacaoRepository
                .findProgramacaoByEquipamentoOrdem(equipamentoId);

        if (programacoes.isEmpty()) {
            return null;
        }

        return programacoes;
    }

    public List<ProgramacaoResponseDTO> buscarTodasProgramacoes() {
        List<Programacao> programacoes = programacaoRepository.findAll();

        if (programacoes.isEmpty()) {
            throw new ProgramacaoNotFoundException("Nenhuma programacao encontrada");
        }

        return programacoes.stream().map(it -> programacaoMapper.toDTODetalhe(it, it.getLote(), it.getEquipamento()))
                .toList();
    }

    @Transactional
    public void deletarProgramacaoPorId(Long id) {
        programacaoRepository.deleteById(id);
    }

    public boolean existirProgamaPorLoteId(Long loteId) {
        return programacaoRepository.existsByLoteId(loteId);
    }

    public Integer buscarMaxFilaDoEquipamento(Long equipamentoId) {
        return programacaoRepository.findMaxFilaByEquipamentoId(equipamentoId);
    }

    @Transactional
    public void resequenciarPrograma(Long id, Long idTtroca) {
        Programacao programacao = buscarProgramacaoPorId(id);
        Programacao programacaoTroca = buscarProgramacaoPorId(idTtroca);

        Long idEquipamentoA = programacao.getEquipamento().getId();
        Long idEquipamentoB = programacaoTroca.getEquipamento().getId();


        if (!idEquipamentoA.equals(idEquipamentoB)) {
            throw new NotProgramacaoValidException(
                    "As programações devem pertencer ao mesmo equipamento para realizar a troca de fila.");
        }
        if (!programacao.getStatus().equals(programacaoTroca.getStatus())) {
            throw new NotProgramacaoValidException(
                    "As programações devem possuir o mesmo status para trocar de fila.");
        }

        int filaA = programacao.getFila();
        int filaB = programacaoTroca.getFila();

        programacao.setFila(0);
        programacaoRepository.saveAndFlush(programacao);

        programacaoTroca.setFila(filaA);
        programacaoRepository.saveAndFlush(programacaoTroca);

        programacao.setFila(filaB);
        programacaoRepository.save(programacao);

    }

}
