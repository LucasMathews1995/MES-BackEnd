package com.example.mes.producao.lote.application;

import org.springframework.stereotype.Service;

import com.example.mes.producao.lote.domain.Lote;
import com.example.mes.producao.lote.domain.exceptions.LoteWithoutOrdemException;
import com.example.mes.producao.lote.domain.exceptions.NotFoundLoteException;
import com.example.mes.producao.lote.infraestructure.dto.LoteOutputDTO;
import com.example.mes.producao.lote.infraestructure.dto.LoteUpdateDTO;
import com.example.mes.producao.lote.infraestructure.persistence.LoteRepository;

import jakarta.transaction.Transactional;

@Service 
public class AlterarAtributosUseCase {

    private final LoteRepository repository;


    public AlterarAtributosUseCase(LoteRepository repository){
        this.repository = repository;
    }

    @Transactional
    public LoteOutputDTO alterarQuantidade(Long id,LoteUpdateDTO dto){
      
        Lote lote = repository.findById(id).orElseThrow(()-> new NotFoundLoteException("Nenhum lote encontrado com esse id :" + id));
        if(lote.getOrdemProducao() != null){
            throw new LoteWithoutOrdemException("O lote já está vinculado a uma ordem de produção");
        }

        if(dto.quantidadeDisponivel()!= null){
        lote.setQuantidadeDisponivel(dto.quantidadeDisponivel());
        }
        
        if(dto.descricao()!= null){
        lote.setDescricao(dto.descricao());
        }

        


        repository.save(lote);

        return LoteOutputDTO.fromEntity(lote);

    }

}
