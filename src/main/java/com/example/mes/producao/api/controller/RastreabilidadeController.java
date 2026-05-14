package com.example.mes.producao.api.controller;

import java.time.LocalDateTime;
import java.util.List;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.example.mes.producao.application.dto.RastreabilidadeDTO;
import com.example.mes.producao.application.service.RastreabilidadeService;
import com.example.mes.producao.domain.StatusRastreabilidade;
import org.springframework.web.bind.annotation.RequestParam;

@RequestMapping("rastreabilidade")
@RestController
@CrossOrigin(origins = "http://localhost:3000")
public class RastreabilidadeController {

    private final RastreabilidadeService rastreabilidadeService;

    public RastreabilidadeController(RastreabilidadeService rastreabilidadeService) {
        this.rastreabilidadeService = rastreabilidadeService;
    }

    @GetMapping
    public ResponseEntity<List<RastreabilidadeDTO>> listarRastreabilidades() {
        return ResponseEntity.ok(rastreabilidadeService.pegarTodasRastreabilidades());
    }

    @GetMapping("/{id}")
    public ResponseEntity<RastreabilidadeDTO> obterRastreabilidadePorId(Long id) {
        return ResponseEntity.ok(rastreabilidadeService.pegarRastreabilidadePorId(id));
    }

    @GetMapping("/pesquisar")
    public ResponseEntity<List<RastreabilidadeDTO>> pesquisar(
            @RequestParam(required = false) String lote,
            @RequestParam(required = false) Long equipamentoId,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dataInicio,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dataFim) {
                
        return ResponseEntity.ok(rastreabilidadeService.buscarRastreabilidadeComFiltros(lote, equipamentoId, status,
                dataInicio, dataFim));
    }

}
