package com.example.mes.producao.ordemproducao.application;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.mes.producao.ordemproducao.domain.OrdemProducao;
import com.example.mes.producao.ordemproducao.exceptions.OrdemProducaoNotFoundException;
import com.example.mes.producao.ordemproducao.infraestructure.persistence.OrdemProducaoRepository;

@Service
public class ExecutarOPUseCase {


    private final OrdemProducaoRepository repository;
  

    public ExecutarOPUseCase (OrdemProducaoRepository repository ){
        this.repository = repository;
      

    }


    @Transactional
    public void executarOP(Long idOP){
        OrdemProducao producao =repository.findById(idOP)
        .orElseThrow(()-> new OrdemProducaoNotFoundException("Ordem de Producao não encontrada"));


        producao.processar();
     

        

    }

}
