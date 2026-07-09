package com.example.mes.producao.programacao.infraestructure.dto;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.example.mes.producao.programacao.application.PorgramacaoUseCase;
import com.example.mes.producao.programacao.domain.StatusProgramacao;

import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;



@RestController
@RequestMapping("api/programacao")
public class ProgramacaoController {

    private final  PorgramacaoUseCase programarUseCase;



    public ProgramacaoController(PorgramacaoUseCase programarUseCase) {
        this.programarUseCase = programarUseCase;
    }
    @PostMapping("/save")
    public ResponseEntity<Void> criarProgramacao(@RequestBody ProgramacaoInputDTO input) {
        programarUseCase.criarProgramacao(input);
     return ResponseEntity.status(HttpStatus.CREATED).build();
    }
    @PatchMapping("/{id}/programar")
    public ResponseEntity<ProgramacaoOutputDTO> programar(@PathVariable Long id) {
        return ResponseEntity.ok(programarUseCase.alterarStatus(id, StatusProgramacao.PROGRAMADA));
    }
     @PatchMapping("/{id}/executar")
    public ResponseEntity<ProgramacaoOutputDTO> executar(@PathVariable Long id) {
        return ResponseEntity.ok(programarUseCase.alterarStatus(id, StatusProgramacao.EM_EXECUCAO));
    }
     @PatchMapping("/{id}/concluir")
    public ResponseEntity<ProgramacaoOutputDTO> concluir(@PathVariable Long id) {
        return ResponseEntity.ok(programarUseCase.alterarStatus(id, StatusProgramacao.CONCLUIDA));
    }
    @PatchMapping("/{id}/cancelar")
    public ResponseEntity<ProgramacaoOutputDTO> cancelar(@PathVariable Long id) {
        return ResponseEntity.ok(programarUseCase.alterarStatus(id, StatusProgramacao.CANCELADA));
    }
      @PatchMapping("/{id}/colocar-qualidade")
    public ResponseEntity<ProgramacaoOutputDTO> colocarQualidade(@PathVariable Long id) {
        return ResponseEntity.ok(programarUseCase.alterarStatus(id, StatusProgramacao.QUALIDADE));
    }
    
}
