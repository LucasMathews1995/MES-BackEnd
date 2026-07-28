package com.example.mes.producao.ordemproducao.application;

import java.util.List;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

import com.example.mes.producao.ordemproducao.domain.OrdemProducao;
import com.example.mes.producao.ordemproducao.dto.OrdemProducaoOuputDTO;
import com.example.mes.producao.ordemproducao.dto.OrdemProducaoPaginadaDTO;
import com.example.mes.producao.ordemproducao.exceptions.OrdemProducaoNotFoundException;
import com.example.mes.producao.ordemproducao.infraestructure.persistence.OrdemProducaoRepository;

@Service 
public class BuscarOrdemProducaoUseCase {


    private final OrdemProducaoRepository repository;



    public BuscarOrdemProducaoUseCase(OrdemProducaoRepository repository){
        this.repository = repository;

    }



    public OrdemProducaoOuputDTO buscarPorId(Long id){
        OrdemProducao op = repository.findById(id).orElseThrow(()-> new OrdemProducaoNotFoundException("Nenhuma ordem de producao com esse id:" + id));

        return OrdemProducaoOuputDTO.fromEntity(op);
    }

    

    public OrdemProducaoPaginadaDTO buscarTodas50(int pagina){

        int tamanhoFixo = 50;
       Pageable pageable = PageRequest.of(pagina, tamanhoFixo);

       Slice<OrdemProducao> slice = repository.findAllByOrderByIdDesc(pageable);

    List<OrdemProducaoOuputDTO> itensDTO = slice.getContent().stream()
            .map(OrdemProducaoOuputDTO::fromEntity)
            .toList();
    

     return new OrdemProducaoPaginadaDTO(itensDTO, slice.hasNext());
    }

}
