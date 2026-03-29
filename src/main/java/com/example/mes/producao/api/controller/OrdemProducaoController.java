package com.example.mes.producao.api.controller;

import com.example.mes.producao.application.dto.OrdemProducaoResponseDTO;
import com.example.mes.producao.application.mapper.OrdemProducaoMapper;
import com.example.mes.producao.application.service.OrdemProducaoProcessamentoService;
import com.example.mes.producao.application.service.OrdemProducaoService;
import com.example.mes.producao.domain.OrdemProducao;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/ordem_producao")
@CrossOrigin("localhost:3000")
@RequiredArgsConstructor
public class OrdemProducaoController {

    private final OrdemProducaoService ordemProducaoService;
    private final OrdemProducaoProcessamentoService ordemProducaoProcessamentoService;
    private final OrdemProducaoMapper ordemProducaoMapper;


    @PostMapping("/save")
    public ResponseEntity<OrdemProducaoResponseDTO> createOrdemProducao(){
        OrdemProducao ordem=  ordemProducaoProcessamentoService.createOrdemProducao();
        OrdemProducaoResponseDTO dto = ordemProducaoMapper.toDTO(ordem);
    return ResponseEntity.ok().body(dto);
    }

    @GetMapping
    public ResponseEntity<List<OrdemProducaoResponseDTO>> getAllOrdemProducao(){
        List<OrdemProducao>  listaOrdemProducao = ordemProducaoService.buscarTodasOrdemProducao();
        List <OrdemProducaoResponseDTO> dto = listaOrdemProducao.stream().map(ordemProducaoMapper::toDTO).toList();

        return ResponseEntity.ok().body(dto);
    }


    @DeleteMapping("{idLote}/{idProd}")
    public ResponseEntity<Void> removeLote(@PathVariable Long idLote, @PathVariable Long idProd){
        ordemProducaoProcessamentoService.desvincularLote(idLote,idProd);

        return ResponseEntity.ok().build();
    }
    @PatchMapping("/{idLote}/{idProd}")
    public ResponseEntity<OrdemProducaoResponseDTO> boundOrdemProducao(@PathVariable Long idLote , @PathVariable Long idProd){
        OrdemProducao op = ordemProducaoProcessamentoService.vincular(idLote, idProd);
        OrdemProducaoResponseDTO dto = ordemProducaoMapper.toDTO(op);

        return new ResponseEntity<>(dto, HttpStatus.OK);
    }





}
