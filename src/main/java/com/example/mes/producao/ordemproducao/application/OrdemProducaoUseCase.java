package com.example.mes.producao.ordemproducao.application;

import java.util.Random;

import org.springframework.stereotype.Service;

import com.example.mes.producao.ordemproducao.domain.OrdemProducao;
import com.example.mes.producao.ordemproducao.dto.OrdemProducaoInputDTO;
import com.example.mes.producao.ordemproducao.dto.OrdemProducaoOuputDTO;
import com.example.mes.producao.ordemproducao.infraestructure.persistence.OrdemProducaoRepository;

import jakarta.transaction.Transactional;

@Service
public class OrdemProducaoUseCase {


    private final OrdemProducaoRepository repository;
    
    public OrdemProducaoUseCase ( OrdemProducaoRepository repository){
        this.repository = repository;
    }


    @Transactional
    public OrdemProducaoOuputDTO salvarOrdemProducao(OrdemProducaoInputDTO dto){

     
     OrdemProducao op = OrdemProducao.criarNormal(dto.equipamentoId(), darNomeOP());
     repository.save(op);   
      return OrdemProducaoOuputDTO.fromEntity(op);
    }

    @Transactional
    public OrdemProducaoOuputDTO salvarRetrabalho (OrdemProducaoInputDTO dto){
        OrdemProducao op = OrdemProducao.criarRetrabalho(dto.equipamentoId(), darNomeOP());
        
        repository.save(op);
       return OrdemProducaoOuputDTO.fromEntity(op);
    }




     


    private String darNomeOP(){
        String  nome;
   
        do{
              String prefixo ="OP";
        int numero = new Random().nextInt(999_999);
         nome = String.format("%s%d", prefixo,numero);
            
        }while(repository.existsByNumeroOP(nome));

        return nome;
    }





   
}
