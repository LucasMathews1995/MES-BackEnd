package com.example.mes.producao.application.service;


import com.example.mes.producao.api.exception.OrdemProducaoNotFoundException;
import com.example.mes.producao.application.dto.OrdemProducaoResponseDTO;
import com.example.mes.producao.application.mapper.OrdemProducaoMapper;
import com.example.mes.producao.domain.Lote;
import com.example.mes.producao.domain.OrdemProducao;
import com.example.mes.producao.domain.StatusOP;

import com.example.mes.producao.infraestructure.OrdemProducaoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.List;
import java.util.Optional;
import java.util.Random;


@Service
@RequiredArgsConstructor
public class OrdemProducaoService {

    private final OrdemProducaoRepository ordemProducaoRepository;
    private final OrdemProducaoMapper ordemProducaoMapper;
    private final Random random= new Random();





    @Transactional
    public OrdemProducao salvarOrdemProducao(OrdemProducao ordemProducao) {
        return   ordemProducaoRepository.save(ordemProducao);
    }

    public OrdemProducao buscarPorId(Long id){
        return ordemProducaoRepository.findById(id).orElseThrow(()-> new OrdemProducaoNotFoundException("Não há ordem de produção para essa ordem " + id));
    }
    public List<OrdemProducao> buscarTodasOrdemProducao(){
        return ordemProducaoRepository.findAll();
    }


    public void deletarOrdemProducao(OrdemProducao ordemProducao){
        ordemProducaoRepository.delete(ordemProducao);
    }
    public boolean existeOrdemProducao (String nome){
       return ordemProducaoRepository.existsByNumeroOP(nome);

    }


    public OrdemProducaoResponseDTO findDtoById(Long id ) {
        OrdemProducao ordem =  ordemProducaoRepository.findById(id)
                .orElseThrow(()-> new OrdemProducaoNotFoundException("Não há ordem de produção para essa ordem " + id));

        return ordemProducaoMapper.toDTO(ordem);

    }





























}
