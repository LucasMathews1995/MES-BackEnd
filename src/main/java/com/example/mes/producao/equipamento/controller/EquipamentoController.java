package com.example.mes.producao.equipamento.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.example.mes.producao.equipamento.dto.EquipamentoOutputDTO;
import com.example.mes.producao.equipamento.dto.EquipamentoPesoUpdateDTO;
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

    @GetMapping("/{id}")
    public ResponseEntity<EquipamentoOutputDTO> buscarEquipamento(@PathVariable Long id) {
        EquipamentoOutputDTO equipamentoBuscado = equipamentoService.buscarEquipamentoPorId(id);
        return ResponseEntity.ok().body(equipamentoBuscado);
    }

    @GetMapping
    public ResponseEntity<List<EquipamentoOutputDTO>> listarTodos() {
        List<EquipamentoOutputDTO> equipamentosBuscados = equipamentoService.buscarEquipamentos();
        return ResponseEntity.ok().body(equipamentosBuscados);
    }

    @PostMapping
    public ResponseEntity<EquipamentoOutputDTO> criarEquipamento(
            @RequestBody @Valid EquipamentoRequestDTO equipamentoRequestDTO) {
        EquipamentoOutputDTO equipamentoCriado = equipamentoService.criarEquipamento(equipamentoRequestDTO);
        return ResponseEntity.ok(equipamentoCriado);
    }

    @PostMapping("/lista")
    public ResponseEntity<List<EquipamentoOutputDTO>> criarEquipamentos(
            @RequestBody @Valid List<EquipamentoRequestDTO> equipamentoRequestDTO) {
        List<EquipamentoOutputDTO> equipamentoCriado = equipamentoService.criarEquipamentoLista(equipamentoRequestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(equipamentoCriado);

    }

    @PatchMapping("/{id}/peso")
    public ResponseEntity<EquipamentoOutputDTO> atualizarPeso(@PathVariable Long id,
            @RequestBody @Valid EquipamentoPesoUpdateDTO dto) {
        EquipamentoOutputDTO equipamento = equipamentoService.atualizarPeso(id, dto);
        return ResponseEntity.ok().body(equipamento);
    }

    @PutMapping("/{id}")
    public ResponseEntity<EquipamentoOutputDTO> atualizarCompleto(@PathVariable Long id,
            @RequestBody @Valid EquipamentoRequestDTO equipamentoRequestDTO) {
        EquipamentoOutputDTO equipamentoAtualizado = equipamentoService.atualizarEquipamento(id, equipamentoRequestDTO);
        return ResponseEntity.ok(equipamentoAtualizado);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        equipamentoService.deletarEquipamento(id);
        return ResponseEntity.noContent().build();
    }
    @PatchMapping("/{id}/desativar")
    public ResponseEntity<Void> desativarEquipamento(@PathVariable Long id) {
        equipamentoService.desativarEquipamento(id);
        return ResponseEntity.noContent().build();
    }
}
