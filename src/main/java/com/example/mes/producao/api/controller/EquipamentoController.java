package com.example.mes.producao.api.controller;



import com.example.mes.producao.application.service.EquipamentoService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;



@RestController
@RequestMapping("/equipamento")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000")
public class EquipamentoController {

    private final EquipamentoService service;


}
