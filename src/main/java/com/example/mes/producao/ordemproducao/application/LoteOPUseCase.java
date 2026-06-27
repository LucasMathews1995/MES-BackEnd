package com.example.mes.producao.ordemproducao.application;

import org.springframework.stereotype.Service;

import com.example.mes.producao.lote.domain.Lote;
import com.example.mes.producao.lote.domain.exceptions.NotFoundLoteException;
import com.example.mes.producao.lote.infraestructure.persistence.LoteRepository;
import com.example.mes.producao.ordemproducao.domain.OrdemProducao;
import com.example.mes.producao.ordemproducao.exceptions.OPNotFoundException;
import com.example.mes.producao.ordemproducao.infraestructure.persistence.OrdemProducaoRepository;

import jakarta.transaction.Transactional;

@Service
public class LoteOPUseCase {


    private final LoteRepository repository;
    private final OrdemProducaoRepository oProducaoRepository;

    public LoteOPUseCase(LoteRepository repository, OrdemProducaoRepository oProducaoRepository){
        this.repository = repository;
        this.oProducaoRepository = oProducaoRepository;
    }



    @Transactional
    public void vincularLoteOP(Long idLote, Long idOP){
        OrdemProducao op = oProducaoRepository.findById(idOP)
        .orElseThrow(()-> new OPNotFoundException("OP não encontrada" + idOP));
        Lote lote = repository.findById(idOP)
        .orElseThrow(()-> new NotFoundLoteException("Nenhum lote encontrado" + idLote));

        op.addLote(lote);


    }

    



}
