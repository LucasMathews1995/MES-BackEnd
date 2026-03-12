package com.example.mes.cliente.api.controller;

import com.example.mes.cliente.application.service.ClienteService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/cliente")
@RequiredArgsConstructor
public class ClienteController {

    private ClienteService clienteService;



}
