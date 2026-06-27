package com.example.mes.producao.ordemproducao.infraestructure.dto;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.mes.producao.ordemproducao.application.LoteOPUseCase;
import com.example.mes.producao.ordemproducao.application.OrdemProducaoUseCase;

import io.swagger.v3.oas.annotations.parameters.RequestBody;
import jakarta.validation.Valid;

@RestController
@RequestMapping("api/ordem_producao")
public class OrdemProducaoController {

    private final OrdemProducaoUseCase useCase;
    private final LoteOPUseCase loteOPUseCase;

    public OrdemProducaoController(OrdemProducaoUseCase useCase,LoteOPUseCase loteOPUseCase) {
        this.useCase = useCase;
        this.loteOPUseCase = loteOPUseCase;
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

   @PostMapping("/{idOP}/lotes/{idLote}")
   public ResponseEntity<Void> vincularLoteOP(@PathVariable Long idLote, @PathVariable Long idOP){
    loteOPUseCase.vincularLoteOP(idLote, idOP);
    return ResponseEntity.ok().build();
   }

}
