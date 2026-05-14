package com.example.mes.producao.api.controller;

import com.example.mes.producao.application.dto.LoteResponseDTO;
import com.example.mes.producao.application.dto.OrdemProducaoResponseDTO;
import com.example.mes.producao.application.facade.ProducaoFacade;
import com.example.mes.producao.application.mapper.LoteMapper;
import com.example.mes.producao.application.mapper.OrdemProducaoMapper;
import com.example.mes.producao.application.service.OrdemProducaoService;
import com.example.mes.producao.domain.Lote;
import com.example.mes.producao.domain.OrdemProducao;

import jakarta.annotation.security.RolesAllowed;
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
    private final OrdemProducaoMapper ordemProducaoMapper;
    private final ProducaoFacade producaoFacade;
    private final LoteMapper loteMapper;


    @PostMapping("/save")
    @RolesAllowed({"Manager", "Administrator"})
    public ResponseEntity<OrdemProducaoResponseDTO> createOrdemProducao(){
        OrdemProducao ordem=  ordemProducaoService.createOrdemProducao();
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
    @RolesAllowed({"Manager", "Administrator","Programador"})
    public ResponseEntity<OrdemProducaoResponseDTO> boundOrdemProducao(@PathVariable Long idLote , @PathVariable Long idProd){
        OrdemProducao op = producaoFacade.vincularOrdemProducaoAoLote(idLote, idProd);
        OrdemProducaoResponseDTO dto = ordemProducaoMapper.toDTO(op);

        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

    @DeleteMapping("/{idProd}")
    @RolesAllowed({"Manager", "Administrator","Programador"})
    public ResponseEntity<OrdemProducaoResponseDTO> removeLote(@PathVariable Long idProd){
        ordemProducaoService.deletarOrdemProducao(idProd);
      return ResponseEntity.noContent().build();
    }
    @DeleteMapping("{idLote}/{idProd}")
    @RolesAllowed({"Manager", "Administrator","Programador"})
    public ResponseEntity<Void> desvinculo(@PathVariable Long idLote, @PathVariable Long idProd){
        ordemProducaoService.desvincularLote(idLote,idProd);

        return ResponseEntity.ok().build();
    }





}
