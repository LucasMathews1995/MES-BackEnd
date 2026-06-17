package com.example.mes.producao.lote.infraestructure.dto;


import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.example.mes.producao.lote.application.CriarLoteUseCase;





@RestController
@RequestMapping("/api/lotes")
public class LoteProducaoController {


    private CriarLoteUseCase criarLoteUseCase;


    public LoteProducaoController(  CriarLoteUseCase criarLoteUseCase) {
        
        this.criarLoteUseCase = criarLoteUseCase;  
      }


    @PostMapping("/save")
    public ResponseEntity<LoteOutputDTO> criarLote(@RequestBody CriarLoteInputDTO input) {
        return ResponseEntity.ok(criarLoteUseCase.executar(input));
    }


}
