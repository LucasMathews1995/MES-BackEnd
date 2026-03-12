package com.example.mes.producao.api.controller;


import com.example.mes.producao.application.dto.EquipamentoRequestDTO;
import com.example.mes.producao.application.dto.EquipamentoResponseDTO;
import com.example.mes.producao.application.service.EquipamentoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.apache.logging.log4j.util.LambdaUtil.getAll;

@RestController
@RequestMapping("/equipamento")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000")
public class EquipamentoController {

    private final EquipamentoService service;

    @PostMapping("/save")
    public ResponseEntity<EquipamentoResponseDTO> salvar(@RequestBody EquipamentoRequestDTO dto){
    EquipamentoResponseDTO equipamento = service.salvar(dto);
    return new ResponseEntity<>(equipamento, HttpStatus.CREATED);
    }


    @GetMapping("/all")
    public ResponseEntity<List<EquipamentoResponseDTO>> listar(){
        List<EquipamentoResponseDTO> equipamentos = service.getAll();

        return  new ResponseEntity<>(equipamentos, HttpStatus.OK);
    }

}
