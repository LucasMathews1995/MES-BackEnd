package com.example.mes.cliente.application.service;

import com.example.mes.cliente.infraestructure.ClienteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ClienteService {


    private final ClienteRepository clienteRepository;




}
