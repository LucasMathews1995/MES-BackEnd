package com.example.mes.producao.application.service;

import com.example.mes.producao.api.exception.AbastecimentoLoteException;
import com.example.mes.producao.api.exception.QualityException;
import com.example.mes.producao.application.dto.LoteRequestDTO;
import com.example.mes.producao.application.dto.LoteResponseDTO;
import com.example.mes.producao.application.mapper.LoteMapper;
import com.example.mes.producao.domain.Lote;
import com.example.mes.producao.domain.OrdemProducao;
import com.example.mes.producao.domain.StatusLote;
import com.example.mes.producao.infraestructure.LoteRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
@RequiredArgsConstructor
public class LoteProcessamentoService {

    private final LoteDataService loteDataService;
    private final OrdemProducaoService ordemProducaoService;
    private final LoteMapper loteMapper;
    private final Random random = new Random() ;
    private final LoteRepository loteRepository;


    @Transactional
    public LoteResponseDTO createLote(LoteRequestDTO dto){

        Lote lote =  loteMapper.toEntity(dto);
        lote.setNome(generateLoteNome());
        lote.setStatus(StatusLote.DESABASTECIDO);

        Lote loteSalvo = loteRepository.save(lote);


        return loteMapper.toDTO(loteSalvo);

    }
    @Transactional
    public void desabastecerLote(Long  id){
        Lote lote = loteDataService.buscarPorId(id);

    if(!lote.getStatus().equals(StatusLote.ABASTECIDO)){
        throw new AbastecimentoLoteException("Lote não está abastecido para desabatecer " + lote.getNome());
    }

        lote.setStatus(StatusLote.DESABASTECIDO);
    }



    @Transactional
    public void abastecerLote(Long id ){
        Lote lote = loteDataService.buscarPorId(id);

        validarTransicaoAbastecimento(lote.getStatus(),StatusLote.ABASTECIDO);

        lote.setStatus(StatusLote.ABASTECIDO);




    }
    @Transactional
    public void produzirLote(Long id ){
    Lote lote =    loteDataService.buscarPorId(id);

        validarTransicaoAbastecimento(lote.getStatus(),StatusLote.PRODUZIDO);
        lote.setStatus(StatusLote.PRODUZIDO);



    }
    @Transactional
    public void aprovarLote(Long id ){
        Lote lote =    loteDataService.buscarPorId(id);

        validarTransicaoAbastecimento(lote.getStatus(),StatusLote.APROVADO);
        lote.setStatus(StatusLote.APROVADO);


    }

    @Transactional
    public void colocarLoteEmqQualidade(Long id ){
        Lote lote =    loteDataService.buscarPorId(id);
       if(lote.getStatus() == StatusLote.APROVADO || lote.getStatus() == StatusLote.PRODUZIDO){
           throw new QualityException("O lote não pode ser colocado em qualidade pois seu status é de : " + lote.getStatus());
       }
       lote.setStatus(StatusLote.QUALIDADE);

        OrdemProducao ordem = lote.getOrdemProducao();

        if(ordem != null){
            ordem.removeLote(lote);
        }

    }
    @Transactional
    public void retirarLoteEmqQualidade(Long id ){
        Lote lote =    loteDataService.buscarPorId(id);

        if(!lote.getStatus().equals(StatusLote.QUALIDADE)){
            throw new QualityException("O lote não pode ser retirado em qualidade pois seu status é de : " + lote.getStatus() + " e ele precisa estar em qualidade");
        }

        lote.setStatus(StatusLote.DESABASTECIDO);

    }




    private void validarTransicaoAbastecimento(StatusLote atual, StatusLote novo) {

        boolean permitida = switch (atual) {
            case DESABASTECIDO -> novo == StatusLote.ABASTECIDO;
            case ABASTECIDO ->  novo == StatusLote.PRODUZIDO ;
            case PRODUZIDO -> novo == StatusLote.APROVADO;

            default -> false;
        };

        if (!permitida) {
            throw new AbastecimentoLoteException("Transição não permitida: " + atual + " -> " + novo);
        }
    }


    private String generateLoteNome(){
        char prefixo = 'A';
        int numeroBase =  random.nextInt(9000);
        String nome =String.format("%s%04d%s", prefixo, numeroBase, "0100");
        while(loteRepository.existsByNome(nome)){
            numeroBase = random.nextInt(9000) ;
            prefixo++;
            nome = String.format("%s%04d%s", prefixo, numeroBase, "0100");
        }
        return nome;

    }







}
