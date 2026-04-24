package com.example.mes.producao.api.controller;


import com.example.mes.producao.application.dto.ProgramacaoOrdemProducaoDTO;
import com.example.mes.producao.application.dto.ProgramacaoRequestDTO;
import com.example.mes.producao.application.dto.ProgramacaoResponseDTO;
import com.example.mes.producao.application.dto.ProgramacaoResumoResponseDTO;
import com.example.mes.producao.application.facade.ProducaoFacade;
import com.example.mes.producao.application.mapper.ProgramacaoMapper;
import com.example.mes.producao.application.service.ProgramacaoService;
import com.example.mes.producao.domain.Programacao;
import com.example.mes.producao.domain.StatusProgramacao;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;


@RestController
@RequestMapping("/programacao")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000")
public class ProgramacaoController {

    private final ProgramacaoService programacaoService;
    private final ProgramacaoMapper programacaoMapper;
    private final ProducaoFacade producaoFacade;




    @PostMapping("/save")
    public ResponseEntity<ProgramacaoResponseDTO> criarProgramacao(@RequestBody @Valid ProgramacaoRequestDTO programacaoRequestDTO) {

        ProgramacaoResponseDTO response = producaoFacade.criarProgramaDoLote(programacaoRequestDTO);

        return ResponseEntity.ok().body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProgramacaoResumoResponseDTO> buscarProgramacao(@PathVariable Long id) {

        Programacao response = programacaoService.buscarProgramacaoPorId(id);
        ProgramacaoResumoResponseDTO responseDTO = programacaoMapper.toDTOResumo(response);

        return ResponseEntity.ok().body(responseDTO);
    }

    @GetMapping("{id}/equipamento_programa_all")
    public ResponseEntity<List<ProgramacaoResponseDTO>> listarProgramacaoParaAbastecer(@PathVariable Long id) {

        List<ProgramacaoResponseDTO> response = programacaoService.buscarProgramacoesPorEquipamentoAteProduzido(id);


        return ResponseEntity.ok().body(response);
    }
    @GetMapping("equipamento/programacao_criada/{id}")
    public ResponseEntity<List<ProgramacaoOrdemProducaoDTO>> listarProgramacaoCriadas(@PathVariable Long id) {
        List<ProgramacaoOrdemProducaoDTO> response  = programacaoService.buscarProgramacaoCriado(id);
        return  ResponseEntity.ok().body(response);
    }

    @GetMapping("/ordem_producao/{equipamentoId}")
    public ResponseEntity<List<ProgramacaoOrdemProducaoDTO>> buscarProgramacaoComOrdemProducao(@PathVariable Long equipamentoId){
        List<ProgramacaoOrdemProducaoDTO> response = programacaoService.buscarProgrmacaoDoEquipamento(equipamentoId);


        return ResponseEntity.ok().body(response);
    }



    @GetMapping
    public ResponseEntity<List<ProgramacaoResponseDTO>> getAllProgramacao() {

        List<ProgramacaoResponseDTO> responseDTO = programacaoService.buscarTodasProgramacoes();


        return ResponseEntity.ok().body(responseDTO);
    }
    @PatchMapping("/{id}/programar")
    public ResponseEntity<ProgramacaoResumoResponseDTO> programarProgramacao(@PathVariable Long id, @RequestBody @Valid ProgramacaoRequestDTO programacaoRequestDTO) {
        ProgramacaoResumoResponseDTO response = producaoFacade.alterarStatus(id, programacaoRequestDTO, StatusProgramacao.PROGRAMADO);

        return ResponseEntity.ok().body(response);
    }


    @PatchMapping("/{id}/abastecer")
    public ResponseEntity<ProgramacaoResumoResponseDTO> abastecerProgramacao(@PathVariable Long id, @RequestBody @Valid ProgramacaoRequestDTO programacaoRequestDTO) {
        ProgramacaoResumoResponseDTO response = producaoFacade.alterarStatus(id, programacaoRequestDTO, StatusProgramacao.ABASTECIDO);

        return ResponseEntity.ok().body(response);
    }

    @PatchMapping("/{id}/produzir")
    public ResponseEntity<ProgramacaoResumoResponseDTO> produzirPrograma(@PathVariable Long id, @RequestBody @Valid ProgramacaoRequestDTO programacaoRequestDTO) {

        ProgramacaoResumoResponseDTO response = producaoFacade.alterarStatus(id, programacaoRequestDTO,StatusProgramacao.PRODUZIDO);

        return ResponseEntity.ok().body(response);
    }

    @PatchMapping("/{id}/aprovar")
    public ResponseEntity<ProgramacaoResumoResponseDTO> aprovarPrograma(@PathVariable Long id, @RequestBody @Valid ProgramacaoRequestDTO programacaoRequestDTO) {

        ProgramacaoResumoResponseDTO response = producaoFacade.alterarStatus(id, programacaoRequestDTO,StatusProgramacao.APROVADO);


        return ResponseEntity.ok().body(response);
    }

    @PatchMapping("/{id}/colocar-qualidade")
    public ResponseEntity<Void> colocarEmQualidade(@PathVariable Long id, @RequestBody @Valid ProgramacaoRequestDTO programacaoRequestDTO) {

        producaoFacade.alterarStatus(id, programacaoRequestDTO,StatusProgramacao.QUALIDADE);


        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/retirar-qualidade")
    public ResponseEntity<ProgramacaoResumoResponseDTO> retirarEmQualidade(@PathVariable Long id,@RequestBody @Valid ProgramacaoRequestDTO programacaoRequestDTO) {

        ProgramacaoResumoResponseDTO response = producaoFacade.retirarDeQualidadeProgramacao(id,programacaoRequestDTO);


        return ResponseEntity.ok().body(response);
    }

    @PutMapping("/{id}/{idTroca}/sequencia")
    public ResponseEntity<Void> trocarSequencia(@PathVariable Long id, @PathVariable Long idTroca) {
        programacaoService.resequenciarPrograma(id, idTroca);
        return ResponseEntity.accepted().build();

    }

    @DeleteMapping("{id}/deletar")
    public ResponseEntity<ProgramacaoResponseDTO> deletarProgramacao(@PathVariable Long id) {
        programacaoService.deletarProgramacaoPorId(id);
        return ResponseEntity.accepted().build();
    }



}
