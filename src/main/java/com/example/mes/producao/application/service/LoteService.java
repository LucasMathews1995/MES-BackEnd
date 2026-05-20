package com.example.mes.producao.application.service;

import com.example.mes.producao.api.exception.*;
import com.example.mes.producao.application.dto.LoteRequestDTO;
import com.example.mes.producao.application.mapper.LoteMapper;
import com.example.mes.producao.domain.*;
import com.example.mes.producao.infraestructure.LoteRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.ObjectInputFilter.Status;
import java.util.List;

import java.util.concurrent.ThreadLocalRandom;

@Service
@RequiredArgsConstructor
public class LoteService {
    private final LoteRepository loteRepository;
    private final LoteMapper loteMapper;
    

    @Transactional
    public Lote salvarLote(Lote lote) {
        return loteRepository.save(lote);

    }

   
    public Lote buscarLotePorId(Long id) {
        return loteRepository.findById(id)
                .orElseThrow(() -> new NotFoundLoteException("Nenhum lote foi encontrado com esse id " + id));
    }

    public List<Lote> findAllLotes() {
        List<Lote> lotes = loteRepository.findAll();

        if (lotes.isEmpty()) {
            throw new NotFoundLoteException("Nenhum lote foi encontrado");
        }
        return lotes;

    }

    public List<Lote> buscarTodosSemOrdemProducao() {
        List<Lote> lotes = loteRepository.findAll().stream().filter(it -> it.getOrdemProducao() == null).toList();

        if (lotes.isEmpty()) {
            throw new NotFoundLoteException("Nenhum lote foi encontrado");
        }
        return lotes;

    }

    public Programacao buscarUltimaProgramacaoPorLote(Long loteId) {
        return loteRepository.buscarUltimaProgramacaoPorLote(loteId).orElseThrow(
                () -> new ProgramacaoNotFoundException("Nenhuma programacao foi encontrado com esse id " + loteId));
    }

    @Transactional
    public Lote criarLote(LoteRequestDTO dto) {
        String nomeUnico = gerarLoteNome();

        Lote lote = loteMapper.toEntity(dto);

        lote.setNome(nomeUnico);

        return loteRepository.save(lote);
    }

    @Transactional
    public Lote abastecerLote(Long id) {
        Lote lote = buscarLotePorId(id);


        lote.abastecerLote();

        return loteRepository.save(lote);
    }

    @Transactional
    public Lote programarLote(Long id) {
        Lote lote = buscarLotePorId(id);

        lote.programarLote();

        return loteRepository.save(lote);
    }

      @Transactional
    public Lote desabastecerLote(Long id) {
        Lote lote = buscarLotePorId(id);

      

        lote.desabastecerLote();

        return loteRepository.save(lote);
    }


    @Transactional
    public Lote produzirLote(Long id) {
        Lote lote = buscarLotePorId(id);

        lote.produzirLote();

        return loteRepository.save(lote);
    }

    @Transactional
    public Lote aprovarLote(Long id) {
        Lote lote = buscarLotePorId(id);

        lote.aprovarLote();

        return loteRepository.save(lote);

    }

    @Transactional
    public Lote colocarLoteEmQualidade(Long id) {

        Lote lote = buscarLotePorId(id);
        Programacao programacao = buscarUltimaProgramacaoPorLote(id);
        lote.enviarParaQualidade(programacao.getQuantidadeConsumida());

        OrdemProducao ordem = lote.getOrdemProducao();

        if (ordem != null) {
            ordem.removeLote(lote);
        }
        return loteRepository.save(lote);
    }

    @Transactional
    public void retirarLoteEmQualidade(Long id,OrdemProducao ordemProducao ) {
        Lote lote = buscarLotePorId(id);

        lote.retirarDeQualidade(ordemProducao);

        loteRepository.save(lote);

    }

    @Transactional
    public void excluirLote(Long id) {
        Lote lote = buscarLotePorId(id);

        lote.validarSePodeSerExcluido();

        loteRepository.delete(lote);

    }


    private String gerarLoteNome() {

        char prefixo = 'A';
        String nomeGerado;
        boolean jaExiste;
        int numeroBase = ThreadLocalRandom.current().nextInt(9000);
        do {

            nomeGerado = String.format("%s%04d%s", prefixo, numeroBase, "0100");
            jaExiste = loteRepository.existsByNome(nomeGerado);

            if (jaExiste) {
                prefixo++;
            }
        } while (jaExiste);

        return String.format("%s%04d%s", prefixo, numeroBase, "0100");
    }

}
