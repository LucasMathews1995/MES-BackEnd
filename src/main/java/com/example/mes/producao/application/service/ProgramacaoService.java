package com.example.mes.producao.application.service;


import com.example.mes.producao.application.mapper.ProgramacaoMapper;
import com.example.mes.producao.infraestructure.ProgramacaoRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class ProgramacaoService {

    private final ProgramacaoRepository programacaoRepository;
    private final OrdemProducaoService ordemProducaoService;
    private final EquipamentoService equipamentoService;
    private final ProgramacaoMapper programacaoMapper;





}
