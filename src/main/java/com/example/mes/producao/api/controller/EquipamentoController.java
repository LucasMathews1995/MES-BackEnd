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
    public ResponseEntity<EquipamentoResponseDTO> criarEquipamento(@Valid @RequestBody EquipamentoRequestDTO equipamentoRequestDTO){
        Equipamento equipamento = service.criarEquipamento(equipamentoRequestDTO);
        return ResponseEntity.ok(mapper.toDTO(equipamento));

    }
    @GetMapping
    @RolesAllowed("Manager")
    public ResponseEntity<List<EquipamentoResponseDTO>> buscarEquipamentos(){
       List<Equipamento> equipamento =  service.buscarEquipamentos();
     List<EquipamentoResponseDTO> dto =   equipamento.stream().sorted(Comparator.comparing(Equipamento::getNome)).map(mapper::toDTO).toList();
        return ResponseEntity.ok(dto);

    }
    @GetMapping("/{id}")
    public ResponseEntity<EquipamentoResponseDTO> buscarEquipamentoPorId(@PathVariable Long id){
        Equipamento equipamento  = service.buscarEquipamentoPorId(id);
        return ResponseEntity.ok(mapper.toDTO(equipamento));
    }





    @DeleteMapping("/{id}/remover")
    public ResponseEntity<Void> removerEquipamento(@PathVariable Long id){

         producaoFacade.deletarEquipamentoComValidacao(id);
         return ResponseEntity.noContent().build();

    }
    @PatchMapping("{id}/desativar")
    public ResponseEntity<Void> desativarEquipamento(@PathVariable Long id){

        service.desativarEquipamento(id);
        return ResponseEntity.noContent().build();

    }
    @PatchMapping("{id}/parar-equipamento")
    public ResponseEntity<Void> pararEquipamento(@PathVariable Long id){
        service.pararEquipamento(id);
        return ResponseEntity.noContent().build();

    }

    @PatchMapping("{id}/ativar_equipamento")
    public ResponseEntity<Void> ativarEquipamento(@PathVariable Long id){
        service.ativarEquipamento(id);
        return ResponseEntity.noContent().build();
    }


}
