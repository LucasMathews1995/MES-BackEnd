package com.example.mes.producao.api.controller;


import com.example.mes.producao.application.dto.ProgramacaoRequestDTO;
import com.example.mes.producao.application.dto.ProgramacaoResponseDTO;
import com.example.mes.producao.application.service.ProgramacaoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/programacao")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000")
public class ProgramacaoController {

    private final ProgramacaoService programacaoService;

    @PostMapping("/save")
    public ResponseEntity<ProgramacaoResponseDTO> salvar(@RequestBody ProgramacaoRequestDTO dto) {
        ProgramacaoResponseDTO response =  programacaoService.inciarProgramacao(dto);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/{equipamentoId}/all")
    public ResponseEntity<List<ProgramacaoResponseDTO>> getAllByEquipamento(@PathVariable Long equipamentoId) {
        List<ProgramacaoResponseDTO> response = programacaoService.recuperarProgramacaoPorEquipamento(equipamentoId);
   return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
