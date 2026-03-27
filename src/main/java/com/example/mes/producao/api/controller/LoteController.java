package com.example.mes.producao.api.controller;

import com.example.mes.producao.application.dto.LoteRequestDTO;
import com.example.mes.producao.application.dto.LoteResponseDTO;
import com.example.mes.producao.application.service.LoteService;

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

    private  final LoteService loteService;


    @PostMapping("/save")
    public ResponseEntity<LoteResponseDTO> createLote(@RequestBody @Valid LoteRequestDTO loteRequestDTO){
        LoteResponseDTO lote = loteService.createLote(loteRequestDTO);


        return  new ResponseEntity<>(lote, HttpStatus.OK);
    }
    @GetMapping
    public ResponseEntity<List<LoteResponseDTO>> getAllLotes(){
        List<LoteResponseDTO> dto  = loteService.findAllLotes();
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<LoteResponseDTO> getLoteById(@PathVariable Long id){
        LoteResponseDTO dto = loteService.getLoteDTOById(id);
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

    @PatchMapping("/{idLote}/{idProd}")
    public ResponseEntity<LoteResponseDTO> boundOrdemProducao(@PathVariable Long idLote , @PathVariable Long idProd){
        LoteResponseDTO dto = loteService.boundOrdemProducao(idLote,idProd);
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }
    @PatchMapping("abastecer/{idLote}")
    public ResponseEntity<Void> abastecerLote(@PathVariable Long idLote,@RequestParam("status") String status){
        loteService.abastecerLote(idLote,status);
        return new ResponseEntity<>( HttpStatus.OK);
    }



}
