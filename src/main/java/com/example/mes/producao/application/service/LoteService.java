package com.example.mes.producao.application.service;

import com.example.mes.producao.infraestructure.EquipamentoRepository;
import com.example.mes.producao.infraestructure.LoteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LoteService {

    private final LoteRepository loteRepository;
}
