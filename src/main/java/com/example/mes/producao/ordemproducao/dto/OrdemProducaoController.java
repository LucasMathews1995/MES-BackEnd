package com.example.mes.producao.ordemproducao.dto;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.example.mes.producao.ordemproducao.application.MudancaEquipamentoUseCase;
import com.example.mes.producao.ordemproducao.application.AlterarAtributosUseCase;
import com.example.mes.producao.ordemproducao.application.BuscarOrdemProducaoUseCase;
import com.example.mes.producao.ordemproducao.application.GerarOrdemProducaoUseCase;
import com.example.mes.producao.ordemproducao.application.VincularLoteUseCase;

import org.springframework.web.bind.annotation.RequestBody;
import jakarta.validation.Valid;


@Validated
@RestController
@RequestMapping("api/ordem_producao")
public class OrdemProducaoController {

    private final GerarOrdemProducaoUseCase gerarOrdemProducaoUseCase;
    private final VincularLoteUseCase vincularLoteUseCase;
    private final MudancaEquipamentoUseCase mudancaEquipamentoUseCase;
    private final AlterarAtributosUseCase atributosUseCase;
    private final BuscarOrdemProducaoUseCase buscarOrdemProducaoUseCase;

    public OrdemProducaoController(GerarOrdemProducaoUseCase useCase, VincularLoteUseCase vincularLoteUseCase,
            MudancaEquipamentoUseCase mudancaEquipamentoUseCase,
            AlterarAtributosUseCase atributosUseCase, BuscarOrdemProducaoUseCase buscarOrdemProducaoUseCase) {
        this.gerarOrdemProducaoUseCase = useCase;
        this.vincularLoteUseCase = vincularLoteUseCase;
        this.mudancaEquipamentoUseCase = mudancaEquipamentoUseCase;
        this.atributosUseCase = atributosUseCase;
        this.buscarOrdemProducaoUseCase = buscarOrdemProducaoUseCase;
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrdemProducaoOuputDTO> buscarPorId(@PathVariable Long id) {
        OrdemProducaoOuputDTO ouputDTO = buscarOrdemProducaoUseCase.buscarPorId(id);
        return ResponseEntity.ok().body(ouputDTO);
    }

    @GetMapping
    public ResponseEntity<OrdemProducaoPaginadaDTO> listarOPs(@RequestParam(defaultValue = "0") int pagina) {

        OrdemProducaoPaginadaDTO resposta = buscarOrdemProducaoUseCase.buscarTodas50(pagina);
        return ResponseEntity.ok(resposta);
    }

    @PostMapping("/normal")
    public ResponseEntity<OrdemProducaoOuputDTO> salvarOP(@RequestBody @Valid OrdemProducaoInputDTO dto) {
        OrdemProducaoOuputDTO ouputDTO = gerarOrdemProducaoUseCase.salvarOrdemProducao(dto);

        return ResponseEntity.ok().body(ouputDTO);
    }

    @PostMapping("/lista-normal")
    public ResponseEntity<List<OrdemProducaoOuputDTO>> criarListaOPS( @RequestBody @Valid List<OrdemProducaoInputDTO> requestList) {
        List<OrdemProducaoOuputDTO> novasOps = gerarOrdemProducaoUseCase.salvarListaOPs(requestList);
        return ResponseEntity.status(HttpStatus.CREATED).body(novasOps);
    }

    @PostMapping("/lista-retrabalho")
    public ResponseEntity<List<OrdemProducaoOuputDTO>> criarListaOPSRetrabalho(   @RequestBody @Valid List<OrdemProducaoInputDTO> requestList) {
        List<OrdemProducaoOuputDTO> novasOps = gerarOrdemProducaoUseCase.salvarRetrabalhoLista(requestList);
        return ResponseEntity.status(HttpStatus.CREATED).body(novasOps);
    }

    @PostMapping("/retrabalho")
    public ResponseEntity<OrdemProducaoOuputDTO> salvarOPRetrabalho(@RequestBody @Valid OrdemProducaoInputDTO dto) {
        OrdemProducaoOuputDTO ouputDTO = gerarOrdemProducaoUseCase.salvarRetrabalho(dto);

        return ResponseEntity.ok().body(ouputDTO);
    }

    @PatchMapping("/vincular/{idOP}/{idLote}")
    public ResponseEntity<OrdemProducaoOuputDTO> vincularLote(@PathVariable Long idOP, @PathVariable Long idLote) {
        OrdemProducaoOuputDTO ouputDTO = vincularLoteUseCase.vincularLote(idOP, idLote);

        return ResponseEntity.ok().body(ouputDTO);
    }

    @PatchMapping("/{id}/equipamento/{equipamentoId}")
    public ResponseEntity<OrdemProducaoOuputDTO> mudarEquipamento(@PathVariable Long id,
            @PathVariable Long equipamentoId) {
        OrdemProducaoOuputDTO ouputDTO = mudancaEquipamentoUseCase.mudarEquipamento(id, equipamentoId);

        return ResponseEntity.ok().body(ouputDTO);
    }

    @PatchMapping("/vincular/{idOP}")
    public ResponseEntity<OrdemProducaoOuputDTO> vincularLotes(@PathVariable Long idOP,
            @RequestBody List<Long> idLotes) {
        OrdemProducaoOuputDTO ouputDTO = vincularLoteUseCase.vincularListaLote(idOP, idLotes);

        return ResponseEntity.ok().body(ouputDTO);
    }

    @PutMapping("/{id}")
    public ResponseEntity<OrdemProducaoOuputDTO> alterarAtributos(@PathVariable Long id,
            @RequestBody @Valid OrdemProducaoUpdateDTO dto) {
        OrdemProducaoOuputDTO ouputDTO = atributosUseCase.alterarAtributos(id, dto);

        return ResponseEntity.ok().body(ouputDTO);
    }

}
