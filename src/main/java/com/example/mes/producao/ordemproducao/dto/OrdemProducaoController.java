package com.example.mes.producao.ordemproducao.dto;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.service.annotation.PatchExchange;

import com.example.mes.producao.ordemproducao.application.OrdemProducaoUseCase;

import org.springframework.web.bind.annotation.RequestBody;
import jakarta.validation.Valid;

@RestController
@RequestMapping("api/ordem_producao")
public class OrdemProducaoController {

    private final OrdemProducaoUseCase useCase;
   

    public OrdemProducaoController(OrdemProducaoUseCase useCase) {
        this.useCase = useCase;
     
    }

    @PostMapping("/normal")
    public ResponseEntity<OrdemProducaoOuputDTO> salvarOP(@RequestBody @Valid OrdemProducaoInputDTO dto) {
        OrdemProducaoOuputDTO ouputDTO = useCase.salvarOrdemProducao(dto);

        return ResponseEntity.ok().body(ouputDTO);
    }

    @PostMapping("/retrabalho")
    public ResponseEntity<OrdemProducaoOuputDTO> salvarOPRetrabalho(@RequestBody @Valid OrdemProducaoInputDTO dto) {
        OrdemProducaoOuputDTO ouputDTO = useCase.salvarRetrabalho(dto);

        return ResponseEntity.ok().body(ouputDTO);
    }


    @PatchMapping("/vincular/{idOP}/{idLote}")
    public ResponseEntity<OrdemProducaoOuputDTO> vincularLote(@PathVariable Long idOP, @PathVariable Long idLote) {
        OrdemProducaoOuputDTO ouputDTO = useCase.vincularLote(idOP, idLote);

        return ResponseEntity.ok().body(ouputDTO);
    }
    @PatchMapping("/{id}/equipamento/{equipamentoId}")
    public ResponseEntity<OrdemProducaoOuputDTO> vincularEquipamento(@PathVariable Long id, @PathVariable Long equipamentoId) {
        OrdemProducaoOuputDTO ouputDTO = useCase.mudarEquipamento(id, equipamentoId);

        return ResponseEntity.ok().body(ouputDTO);
    }

 

}
