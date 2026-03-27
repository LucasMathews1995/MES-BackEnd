package com.example.mes.producao.application.service;

import com.example.mes.producao.api.exception.NotFoundLoteException;
import com.example.mes.producao.api.exception.OrdemAndLoteException;
import com.example.mes.producao.api.exception.ProgramacaoNotFoundException;
import com.example.mes.producao.application.dto.ProgramacaoRequestDTO;
import com.example.mes.producao.application.dto.ProgramacaoResponseDTO;
import com.example.mes.producao.application.mapper.ProgramacaoMapper;
import com.example.mes.producao.domain.*;
import com.example.mes.producao.infraestructure.ProgramacaoRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@AllArgsConstructor
public class ProgramacaoService {

    private final ProgramacaoRepository programacaoRepository;
    private final OrdemProducaoService ordemProducaoService;
    private final EquipamentoService equipamentoService;
    private final ProgramacaoMapper programacaoMapper;





}
