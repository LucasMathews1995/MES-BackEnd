package com.example.mes.producao.lote.infraestructure.dto;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.mes.producao.lote.application.AlterarAtributosUseCase;
import com.example.mes.producao.lote.application.BuscarLotesUseCase;
import com.example.mes.producao.lote.application.CriarLoteUseCase;

@RestController
@RequestMapping("/api/lotes")
public class LoteProducaoController {

    private final CriarLoteUseCase criarLoteUseCase;
    private final BuscarLotesUseCase buscarLotesUseCase;
    private final AlterarAtributosUseCase alterarAtributosUseCase;

    public LoteProducaoController(CriarLoteUseCase criarLoteUseCase, BuscarLotesUseCase buscarLotesUseCase,AlterarAtributosUseCase alterarAtributosUseCase) {

        this.criarLoteUseCase = criarLoteUseCase;
        this.buscarLotesUseCase = buscarLotesUseCase;
        this.alterarAtributosUseCase = alterarAtributosUseCase;
    }

    @PostMapping("/save")
    public ResponseEntity<LoteOutputDTO> criarLote(@RequestBody CriarLoteInputDTO input) {
        return ResponseEntity.ok(criarLoteUseCase.executar(input));
    }

    @GetMapping("/{id}")
    public ResponseEntity<LoteOutputDTO> buscarLotesPorId(@PathVariable Long id) {
        return ResponseEntity.ok(buscarLotesUseCase.buscarLotesPorId(id));
    }

    @GetMapping("/{idOP}")
    public ResponseEntity<List<LoteOutputDTO>> buscarLotesPorOrdem(@PathVariable Long idOP) {
        return ResponseEntity.ok(buscarLotesUseCase.buscarLotesPorOrdemPorducao(idOP));
    }

    @GetMapping()
    public ResponseEntity<List<LoteOutputDTO>> buscarLotesSemOrdem() {
        return ResponseEntity.ok(buscarLotesUseCase.buscarLotesSemOrdemProducao());
    }

    @PutMapping("/{id}")
    public ResponseEntity<LoteOutputDTO> alterarLote(@PathVariable Long id , @RequestBody LoteUpdateDTO dto){
        return ResponseEntity.ok(alterarAtributosUseCase.alterarQuantidade(id, dto));
    }

    

}
