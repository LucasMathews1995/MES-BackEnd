package com.example.mes.producao.ordemproducao.application;

import org.springframework.stereotype.Service;

import com.example.mes.producao.ordemproducao.domain.OrdemProducao;
import com.example.mes.producao.ordemproducao.dto.OrdemProducaoOuputDTO;
import com.example.mes.producao.ordemproducao.dto.OrdemProducaoUpdateDTO;
import com.example.mes.producao.ordemproducao.exceptions.OrdemProducaoNotFoundException;
import com.example.mes.producao.ordemproducao.infraestructure.persistence.OrdemProducaoRepository;

import jakarta.transaction.Transactional;

@Service 
public class AlterarAtributosUseCase {

    private final OrdemProducaoRepository repository;


    public AlterarAtributosUseCase ( OrdemProducaoRepository repository){
        this.repository = repository;
    }


    @Transactional
    public OrdemProducaoOuputDTO alterarAtributos (Long id , OrdemProducaoUpdateDTO dto ){

        OrdemProducao ordemProducao =repository.findById(id).orElseThrow(()-> new OrdemProducaoNotFoundException("Nenhuma ordem de produção encontrada com esse id: "+ id));

        

        ordemProducao.setCapacidadeMaxima(dto.capacidadeMaxima());


        repository.save(ordemProducao);

        return OrdemProducaoOuputDTO.fromEntity(ordemProducao);
        
    }

}
