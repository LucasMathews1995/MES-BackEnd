package com.example.mes.producao.api.controller;

import com.example.mes.producao.application.dto.EquipamentoRequestDTO;
import com.example.mes.producao.application.dto.EquipamentoResponseDTO;
import com.example.mes.producao.application.facade.ProducaoFacade;
import com.example.mes.producao.application.mapper.EquipamentoMapper;
import com.example.mes.producao.application.service.EquipamentoService;
import com.example.mes.producao.domain.Equipamento;
import jakarta.annotation.security.RolesAllowed;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Comparator;
import java.util.List;

@RestController
@RequestMapping("/equipamento")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000")
public class EquipamentoController {

    private final EquipamentoService service;
    private final EquipamentoMapper mapper;
    private final ProducaoFacade producaoFacade;

    @PostMapping("/salvar")
    @RolesAllowed({ "Manager", "Administrator" })
    public ResponseEntity<EquipamentoResponseDTO> criarEquipamento(
            @Valid @RequestBody EquipamentoRequestDTO equipamentoRequestDTO) {
        Equipamento equipamento = service.criarEquipamento(equipamentoRequestDTO);
        return ResponseEntity.ok(mapper.toDTO(equipamento));

    }

    @GetMapping
    public ResponseEntity<List<EquipamentoResponseDTO>> buscarEquipamentos() {
        List<Equipamento> equipamento = service.buscarEquipamentos();
        List<EquipamentoResponseDTO> dto = equipamento.stream().sorted(Comparator.comparing(Equipamento::getNome))
                .map(mapper::toDTO).toList();
        return ResponseEntity.ok(dto);

    }

    @GetMapping("/{id}")
    public ResponseEntity<EquipamentoResponseDTO> buscarEquipamentoPorId(@PathVariable Long id) {
        Equipamento equipamento = service.buscarEquipamentoPorId(id);
        return ResponseEntity.ok(mapper.toDTO(equipamento));
    }

    @DeleteMapping("/{id}/remover")
    @RolesAllowed({ "Manager", "Administrator" })
    public ResponseEntity<Void> removerEquipamento(@PathVariable Long id) {

        service.deletarEquipamento(id);
        return ResponseEntity.noContent().build();

    }

    @PatchMapping("{id}/desativar")
    @RolesAllowed("Manager")
    public ResponseEntity<Void> desativarEquipamento(@PathVariable Long id) {

        producaoFacade.desativarEquipamento(id);
        return ResponseEntity.noContent().build();

    }

    @PatchMapping("{id}/ativar_equipamento")
    @RolesAllowed({ "Manager", "Administrator" })
    public ResponseEntity<Void> ativarEquipamento(@PathVariable Long id) {
        producaoFacade.ativarEquipamento(id);
        return ResponseEntity.noContent().build();
    }

}
