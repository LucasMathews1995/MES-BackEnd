package com.example.mes.producao.application.service;


import com.example.mes.producao.api.exception.AbastecimentoLoteException;
import com.example.mes.producao.api.exception.NotFoundLoteException;
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

import java.util.List;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class LoteService {

    private final LoteRepository loteRepository;
    private final LoteMapper loteMapper;
    private final Random random = new Random();
    private final OrdemProducaoService ordemProducaoService ;



    @Transactional
    public LoteResponseDTO createLote(LoteRequestDTO dto){

      Lote lote =  loteMapper.toEntity(dto);
       lote.setNome(generateLoteNome());
       lote.setStatus(StatusLote.CRIADO);

       Lote loteSalvo = loteRepository.save(lote);


      return loteMapper.toDTO(loteSalvo);

    }


    public LoteResponseDTO getLoteDTOById(long id){
       return loteMapper.toDTO(getLoteEntityById(id));
    }

    public List<LoteResponseDTO> findAllLotes(){
        List<Lote> lotes = loteRepository.findAll();

        return lotes.stream().map(loteMapper::toDTO).toList();
    }


    @Transactional
    public void abastecerLote(Long id , String status){
        Lote lote = getLoteEntityById(id);
        StatusLote statusNovo = StatusLote.valueOf(status.toUpperCase());
        validarTransicao(lote.getStatus(),statusNovo);

        lote.setStatus(statusNovo);


         loteRepository.save(lote);



    }

    @Transactional
    public LoteResponseDTO boundOrdemProducao(Long idLote, Long idProd){
        Lote lote = getLoteEntityById(idLote);
        OrdemProducao ordemProducao = ordemProducaoService.getEntityById(idProd);
        lote.setOrdemProducao(ordemProducao);

        return loteMapper.toDTO(lote);
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

        private Lote getLoteEntityById(long id){
        return  loteRepository.findById(id).orElseThrow(()-> new NotFoundLoteException("Nenhum lote foi encontrado com esse id "+ id));
        }

    private void validarTransicao(StatusLote atual, StatusLote novo) {

        boolean permitida = switch (atual) {
            case CRIADO -> novo == StatusLote.ABASTECIDO;
            case ABASTECIDO ->  novo == StatusLote.PRODUZIDO ;
            case PRODUZIDO -> novo == StatusLote.APROVADO;

            default -> false;
        };

        if (!permitida) {
            throw new AbastecimentoLoteException("Transição não permitida: " + atual + " -> " + novo);
        }
    }

    }

