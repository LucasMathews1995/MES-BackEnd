package com.example.mes.producao.application.service;


import com.example.mes.producao.api.exception.LoteBoundsException;
import com.example.mes.producao.api.exception.NotFoundLoteException;
import com.example.mes.producao.application.dto.LoteRequestDTO;
import com.example.mes.producao.application.dto.LoteResponseDTO;
import com.example.mes.producao.application.mapper.LoteMapper;
import com.example.mes.producao.domain.Lote;
import com.example.mes.producao.infraestructure.LoteRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class LoteService {

    private final LoteRepository loteRepository;
    private final LoteMapper loteMapper;

    @Transactional
    public LoteResponseDTO createLote(LoteRequestDTO dto){

        Lote lote = loteMapper.toEntity(dto);
        lote.setLoteNome(darNomeAoLote());


        loteRepository.save(lote);



        return loteMapper.toDTO(lote);
    }

    @Transactional
    public List<LoteResponseDTO> createAllLotes(List<LoteRequestDTO> dto){

        List<Lote> lotes = dto.stream().map(e-> {

         Lote lote =  loteMapper.toEntity(e);
         lote.setLoteNome(darNomeAoLote());
         return lote;
        }).toList();

        loteRepository.saveAll(lotes);

        return lotes.stream().map(loteMapper::toDTO).toList();
    }

    public  Lote getById (Long id){


        return loteRepository.findById(id).orElseThrow(()-> new NotFoundLoteException("Nenhum lote encontrado"));
    }

    @Transactional
    public Lote atualizarEstoque(Long id, Integer quantidadeConsumida) {
        Lote lote = loteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Lote não encontrado"));

        lote.consumirQuantidade(quantidadeConsumida);

        return loteRepository.save(lote);
    }


    private String darNomeAoLote(){
        char prefixo  = 'A';
        Integer numeroBase = new Random().nextInt(100000);
        String numeroString = String.valueOf(numeroBase);
        String nomeCandidato = prefixo  + numeroString;

        while(loteRepository.existsByLoteNome(nomeCandidato)){
            prefixo++;
            nomeCandidato = prefixo  + numeroString;
            if (prefixo > 'Z') {
                throw new LoteBoundsException("Limite de variações (A-Z) atingido para este nome.");
            }
        }
        return nomeCandidato;


    }







}
