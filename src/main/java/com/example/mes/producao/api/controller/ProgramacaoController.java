package com.example.mes.producao.api.controller;


import com.example.mes.producao.application.dto.ProgramacaoRequestDTO;
import com.example.mes.producao.application.dto.ProgramacaoResponseDTO;
import com.example.mes.producao.application.mapper.ProgramacaoMapper;
import com.example.mes.producao.application.service.ProgramacaoProcessamentoService;
import com.example.mes.producao.application.service.ProgramacaoService;
import com.example.mes.producao.domain.Programacao;
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

    private final ProgramacaoProcessamentoService programacaoProcessamentoService;
    private final ProgramacaoService programacaoService;
    private final ProgramacaoMapper programacaoMapper;


    @PostMapping("/save")
    public ResponseEntity<ProgramacaoResponseDTO> criarProgramacao(@RequestBody @Valid ProgramacaoRequestDTO programacaoRequestDTO) {

        Programacao response = programacaoProcessamentoService.programarProgramacao(programacaoRequestDTO);
        ProgramacaoResponseDTO responseDTO = programacaoMapper.toDTO(response);

        return ResponseEntity.ok().body(responseDTO);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProgramacaoResponseDTO> buscarProgramacao(@PathVariable Long id) {

        Programacao response = programacaoService.buscarProgramacaoPorId(id);
        ProgramacaoResponseDTO responseDTO = programacaoMapper.toDTO(response);

        return ResponseEntity.ok().body(responseDTO);
    }

    @GetMapping
    public ResponseEntity<List<ProgramacaoResponseDTO>> getAllProgramacao() {

        List<Programacao> response = programacaoService.buscarTodasProgramacoes();
        List<ProgramacaoResponseDTO> responseDTO = response.stream().map(programacaoMapper::toDTO).toList();

        return ResponseEntity.ok().body(responseDTO);
    }

    @PatchMapping("/{id}/abastecer")
    public ResponseEntity<ProgramacaoResponseDTO> abastecerProgramacao(@PathVariable Long id, @RequestBody @Valid ProgramacaoRequestDTO programacaoRequestDTO) {
        Programacao response = programacaoProcessamentoService.abastecerProgramacao(id, programacaoRequestDTO);
        ProgramacaoResponseDTO responseDTO = programacaoMapper.toDTO(response);
        return ResponseEntity.ok().body(responseDTO);
    }

    @PatchMapping("/{id}/produzir")
    public ResponseEntity<ProgramacaoResponseDTO> produzirPrograma(@PathVariable Long id, @RequestBody @Valid ProgramacaoRequestDTO programacaoRequestDTO) {

        Programacao response = programacaoProcessamentoService.produzirPrograma(id, programacaoRequestDTO);
        ProgramacaoResponseDTO responseDTO = programacaoMapper.toDTO(response);
        return ResponseEntity.ok().body(responseDTO);
    }

    @PatchMapping("/{id}/aprovar")
    public ResponseEntity<ProgramacaoResponseDTO> aprovarPrograma(@PathVariable Long id, @RequestBody @Valid ProgramacaoRequestDTO programacaoRequestDTO) {

        Programacao response = programacaoProcessamentoService.aprovarPrograma(id, programacaoRequestDTO);
        ProgramacaoResponseDTO responseDTO = programacaoMapper.toDTO(response);

        return ResponseEntity.ok().body(responseDTO);
    }

    @PatchMapping("/{id}/colocar-qualidade")
    public ResponseEntity<Void> colocarEmQualidade(@PathVariable Long id, @RequestBody @Valid ProgramacaoRequestDTO programacaoRequestDTO) {

      programacaoProcessamentoService.colocarEmQualidadeProgramacao(id, programacaoRequestDTO);


        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/retirar-qualidade")
    public ResponseEntity<ProgramacaoResponseDTO> retirarEmQualidade(@RequestBody @Valid ProgramacaoRequestDTO programacaoRequestDTO) {

        Programacao response = programacaoProcessamentoService.retirarDeQualidadeProgramacao(programacaoRequestDTO);
        ProgramacaoResponseDTO responseDTO = programacaoMapper.toDTO(response);

        return ResponseEntity.ok().body(responseDTO);
    }

    @PutMapping("/{id}/{idTroca}/sequencia")
    public ResponseEntity<Void> trocarSequencia(@PathVariable Long id, @PathVariable Long idTroca) {
        programacaoProcessamentoService.resequenciarPrograma(id, idTroca);
        return ResponseEntity.accepted().build();

    }

    @DeleteMapping("{id}/deletar")
    public ResponseEntity<ProgramacaoResponseDTO> deletarProgramacao(@PathVariable Long id) {
        programacaoService.deletarProgramacaoPorId(id);
        return ResponseEntity.accepted().build();
    }



}
