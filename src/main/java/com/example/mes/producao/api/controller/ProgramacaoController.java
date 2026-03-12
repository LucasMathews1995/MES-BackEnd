package com.example.mes.producao.api.controller;

import com.example.mes.producao.application.dto.LoteRequestDTO;
import com.example.mes.producao.application.dto.LoteResponseDTO;
import com.example.mes.producao.application.dto.ProgramacaoRequestDTO;
import com.example.mes.producao.application.dto.ProgramacaoResponseDTO;
import com.example.mes.producao.application.service.ProgramacaoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/programacao")
@RequiredArgsConstructor
@CrossOrigin
public class ProgramacaoController {

    private final ProgramacaoService programacaoService;

    @PostMapping("/save")
    public ResponseEntity<ProgramacaoResponseDTO> salvar(@RequestBody ProgramacaoRequestDTO dto) {
        ProgramacaoResponseDTO response =  programacaoService.inciarProgramacao(dto);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
}
