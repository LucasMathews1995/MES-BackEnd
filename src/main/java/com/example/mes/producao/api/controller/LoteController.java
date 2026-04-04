package com.example.mes.producao.api.controller;

import com.example.mes.producao.application.dto.LoteRequestDTO;
import com.example.mes.producao.application.dto.LoteResponseDTO;
import com.example.mes.producao.application.mapper.LoteMapper;
import com.example.mes.producao.application.service.LoteDataService;

import com.example.mes.producao.application.service.LoteProcessamentoService;
import com.example.mes.producao.domain.Lote;
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
    public ResponseEntity<LoteResponseDTO> criarLote(@RequestBody @Valid LoteRequestDTO loteRequestDTO){
        Lote response = loteProcessamentoService.criarLote(loteRequestDTO);
        LoteResponseDTO dto =   loteMapper.toDTO(response);

        return  new ResponseEntity<>(dto, HttpStatus.OK);
    }
    @GetMapping
    public ResponseEntity<List<LoteResponseDTO>> buscarTodos(){
        List<LoteResponseDTO> dto  = loteDataService.findAllLotes().stream().map(loteMapper::toDTO).toList();
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<LoteResponseDTO> buscarPorId(@PathVariable Long id){
        LoteResponseDTO dto = loteMapper.toDTO(loteDataService.buscarLotePorId(id));
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }
    @GetMapping("/sem-op")
    public ResponseEntity<List<LoteResponseDTO>> buscarTodosLotesSemOrdemProducao(){
        List<Lote> lotes =loteDataService.buscarTodosSemOrdemProducao();
       List<LoteResponseDTO> response = lotes.stream().map(loteMapper::toDTO).toList();
       return  new ResponseEntity<>(response, HttpStatus.OK);
    }
    @PatchMapping("/{id}/desabastecer")
    public ResponseEntity<Void> desabastecerPorId(@PathVariable Long id){
        loteProcessamentoService.desabastecerLote(id);

        return ResponseEntity.ok().build();

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarLote(@PathVariable Long id){

        loteProcessamentoService.excluirLote(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }







}
