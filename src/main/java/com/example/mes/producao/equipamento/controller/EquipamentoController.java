package com.example.mes.producao.equipamento.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.example.mes.producao.equipamento.dto.EquipamentoOutputDTO;
import com.example.mes.producao.equipamento.dto.EquipamentoRequestDTO;
import com.example.mes.producao.equipamento.service.EquipamentoService;

import jakarta.validation.Valid;


@RestController
@RequestMapping("/api/equipamentos")
public class EquipamentoController {

    private EquipamentoService equipamentoService;

        public EquipamentoController(EquipamentoService equipamentoService) {
            this.equipamentoService = equipamentoService;
        }


        @PostMapping("/save")
        public ResponseEntity<EquipamentoOutputDTO> criarEquipamento(@RequestBody @Valid EquipamentoRequestDTO equipamentoRequestDTO) {
            EquipamentoOutputDTO equipamentoCriado = equipamentoService.criarEquipamento(equipamentoRequestDTO);
            return ResponseEntity.ok(equipamentoCriado);
        }


        @PatchMapping("/update/{id}")
        public ResponseEntity<EquipamentoOutputDTO> atualizarEquipamento(@PathVariable Long id, @RequestBody @Valid EquipamentoRequestDTO equipamentoRequestDTO) {
            EquipamentoOutputDTO equipamentoAtualizado = equipamentoService.atualizarEquipamento(id, equipamentoRequestDTO);
            return ResponseEntity.ok(equipamentoAtualizado);
        }
}
