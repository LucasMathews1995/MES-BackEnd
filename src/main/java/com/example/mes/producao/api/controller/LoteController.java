package com.example.mes.producao.api.controller;

import com.example.mes.producao.application.dto.LoteRequestDTO;
import com.example.mes.producao.application.dto.LoteResponseDTO;
import com.example.mes.producao.application.mapper.LoteMapper;
import com.example.mes.producao.application.service.LoteDataService;

import com.example.mes.producao.application.service.LoteProcessamentoService;
import jakarta.validation.Valid;
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

    private  final LoteDataService loteDataService;
    private final LoteProcessamentoService loteProcessamentoService;
    private final LoteMapper loteMapper;


    @PostMapping("/save")
    public ResponseEntity<LoteResponseDTO> createLote(@RequestBody @Valid LoteRequestDTO loteRequestDTO){
        LoteResponseDTO lote = loteProcessamentoService.createLote(loteRequestDTO);

        return  new ResponseEntity<>(lote, HttpStatus.OK);
    }
    @GetMapping
    public ResponseEntity<List<LoteResponseDTO>> getAllLotes(){
        List<LoteResponseDTO> dto  = loteDataService.findAllLotes().stream().map(loteMapper::toDTO).toList();
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<LoteResponseDTO> getLoteById(@PathVariable Long id){
        LoteResponseDTO dto = loteMapper.toDTO(loteDataService.buscarPorId(id));
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }


    @PatchMapping("{idLote}/abastecer")
    public ResponseEntity<Void> abastecerLote(@PathVariable Long idLote){
        loteProcessamentoService.abastecerLote(idLote);
        return new ResponseEntity<>( HttpStatus.OK);
    }

    @PatchMapping("{idLote}/produzir")
    public ResponseEntity<Void> produzirLote(@PathVariable Long idLote){
        loteProcessamentoService.abastecerLote(idLote);
        return new ResponseEntity<>( HttpStatus.OK);
    }

    @PatchMapping("/{idLote}/inspecao")
    public ResponseEntity<Void> colocarEmQualidade(@PathVariable Long idLote){
        loteProcessamentoService.colocarLoteEmqQualidade(idLote);
        return new ResponseEntity<>( HttpStatus.OK);
    }

    @PatchMapping("/{idLote}/liberacao")
    public ResponseEntity<Void> retirarDeLote(@PathVariable Long idLote){
        loteProcessamentoService.retirarLoteEmqQualidade(idLote);
        return new ResponseEntity<>( HttpStatus.OK);
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarLote(@PathVariable Long id){

        loteDataService.deleteLoteById(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }







}
