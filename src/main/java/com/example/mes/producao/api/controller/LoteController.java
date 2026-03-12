package com.example.mes.producao.api.controller;

import com.example.mes.producao.application.dto.LoteRequestDTO;
import com.example.mes.producao.application.dto.LoteResponseDTO;
import com.example.mes.producao.application.service.LoteService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/lote")
@RequiredArgsConstructor
@CrossOrigin
public class LoteController {

    private final LoteService loteService;


    @PostMapping("/save")
    public ResponseEntity<LoteResponseDTO> salvar(@RequestBody LoteRequestDTO dto) {
    LoteResponseDTO response =  loteService.createLote(dto);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PostMapping("saveAll")
    public ResponseEntity<List<LoteResponseDTO>> saveAll(@RequestBody List<LoteRequestDTO> dto) {
        List<LoteResponseDTO> response =  loteService.createAllLotes(dto);
        return new ResponseEntity<>(response,HttpStatus.CREATED);
    }

}
