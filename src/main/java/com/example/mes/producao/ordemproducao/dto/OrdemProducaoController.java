package com.example.mes.producao.ordemproducao.dto;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


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

 

}
