package com.example.mes.producao.ordemproducao.application;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.example.mes.producao.lote.domain.Lote;
import com.example.mes.producao.lote.domain.exceptions.NotFoundLoteException;
import com.example.mes.producao.lote.infraestructure.persistence.LoteRepository;
import com.example.mes.producao.ordemproducao.domain.OrdemProducao;
import com.example.mes.producao.ordemproducao.dto.OrdemProducaoOuputDTO;
import com.example.mes.producao.ordemproducao.exceptions.OrdemProducaoNotFoundException;
import com.example.mes.producao.ordemproducao.infraestructure.persistence.OrdemProducaoRepository;

import jakarta.transaction.Transactional;



@Service
public class VincularLoteUseCase {


    private final OrdemProducaoRepository repository;
    private final LoteRepository loteRepository;

    public VincularLoteUseCase(OrdemProducaoRepository repository, LoteRepository loteRepository) {
        this.repository = repository;
        this.loteRepository = loteRepository;   }


     @Transactional
    public OrdemProducaoOuputDTO vincularLote(Long idOP, Long idLote){
      OrdemProducao op = repository.findById(idOP).orElseThrow(() -> new OrdemProducaoNotFoundException("Ordem de produção não encontrada"));
        Lote lote = loteRepository.findById(idLote).orElseThrow(() -> new NotFoundLoteException("Lote não encontrado"));

        op.adicionarLote(lote);

        repository.save(op);
        loteRepository.save(lote);

        return OrdemProducaoOuputDTO.fromEntity(op);
    }

    @Transactional
    public OrdemProducaoOuputDTO vincularListaLote(Long idOP, List<Long> idLotes){
       OrdemProducao op = repository.findById(idOP)
       .orElseThrow(() -> new OrdemProducaoNotFoundException("Ordem de produção não encontrada"));
       
        List<Lote> lotes = loteRepository.findAllById(idLotes);
        
   

    
     if (lotes.isEmpty() || lotes.size() != idLotes.size()) {
        
      
        String idsRecebidos = idLotes.stream()
            .map(String::valueOf)
            .collect(Collectors.joining(", "));
            
       throw new NotFoundLoteException("Um ou mais lotes não foram encontrados no sistema. IDs solicitados: " + idsRecebidos);
    }


        op.adicionarListadeLotes(lotes);
   
    repository.save(op);  

   return OrdemProducaoOuputDTO.fromEntity(op);

    }



}
