package com.example.mes.producao.api.controller;

import com.example.mes.producao.application.dto.LoteResponseDTO;
import com.example.mes.producao.application.dto.OrdemProducaoResponseDTO;
import com.example.mes.producao.application.mapper.LoteMapper;
import com.example.mes.producao.application.mapper.OrdemProducaoMapper;
import com.example.mes.producao.application.service.OrdemProducaoProcessamentoService;
import com.example.mes.producao.application.service.OrdemProducaoService;
import com.example.mes.producao.domain.Lote;
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
    private final LoteMapper loteMapper;


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
    @GetMapping("/{id}")
    public ResponseEntity<OrdemProducaoResponseDTO> getOrdemProducao(@PathVariable Long id){
        OrdemProducao op = ordemProducaoService.buscarPorId(id);

        return ResponseEntity.ok().body(ordemProducaoMapper.toDTO(op));
    }

    @GetMapping("{id}/lotes")
    public ResponseEntity<List<LoteResponseDTO>> listarLotesDaOrdem(@PathVariable Long id){
        List<Lote> lotes = ordemProducaoService.buscarLotesPorOrdemProducao(id);
        List<LoteResponseDTO> dto = lotes.stream().map(loteMapper::toDTO).toList();

            return ResponseEntity.ok(dto);

    }

    @PatchMapping("/{idLote}/{idProd}")
    public ResponseEntity<OrdemProducaoResponseDTO> boundOrdemProducao(@PathVariable Long idLote , @PathVariable Long idProd){
        OrdemProducao op = ordemProducaoProcessamentoService.vincular(idLote, idProd);
        OrdemProducaoResponseDTO dto = ordemProducaoMapper.toDTO(op);

        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

    @DeleteMapping("/{idProd}")
    public ResponseEntity<OrdemProducaoResponseDTO> removeLote(@PathVariable Long idProd){
        ordemProducaoProcessamentoService.deletarOrdemProducao(idProd);
      return ResponseEntity.noContent().build();
    }
    @DeleteMapping("{idLote}/{idProd}")
    public ResponseEntity<Void> removeLote(@PathVariable Long idLote, @PathVariable Long idProd){
        ordemProducaoProcessamentoService.desvincularLote(idLote,idProd);

        return ResponseEntity.ok().build();
    }





}
