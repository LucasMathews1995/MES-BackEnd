package com.example.mes.producao.programacao.infraestructure.dto;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.mes.producao.programacao.application.CriacaodeProgramacaoUseCase;
import com.example.mes.producao.programacao.application.AlterarProgramacaoUseCase;
import com.example.mes.producao.programacao.application.ColocarRetirarQualidadeUseCase;
import com.example.mes.producao.programacao.domain.StatusProgramacao;

import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;



@RestController
@RequestMapping("api/programacao")
public class ProgramacaoController {

    private final  AlterarProgramacaoUseCase programarUseCase;
    private final CriacaodeProgramacaoUseCase criacaodeProgramacaoUseCase;
    private final ColocarRetirarQualidadeUseCase colocarRetirarQualidadeUseCase;


    public ProgramacaoController(AlterarProgramacaoUseCase programarUseCase,CriacaodeProgramacaoUseCase criacaodeProgramacaoUseCase, ColocarRetirarQualidadeUseCase colocarRetirarQualidadeUseCase) {
        this.programarUseCase = programarUseCase;
        this.criacaodeProgramacaoUseCase = criacaodeProgramacaoUseCase;
        this.colocarRetirarQualidadeUseCase = colocarRetirarQualidadeUseCase;
    }
    @PostMapping
    public ResponseEntity<List<ProgramacaoOutputDTO>> criarProgramacao(@RequestBody ProgramacaoInputDTO input) {
        
     return ResponseEntity.status(HttpStatus.CREATED).body(criacaodeProgramacaoUseCase.criarProgramacao(input));
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
        return ResponseEntity.ok(colocarRetirarQualidadeUseCase.alterarStatusQualidade(id, StatusProgramacao.QUALIDADE));
    }
        @PatchMapping("/{id}/retirar-qualidade")
    public ResponseEntity<ProgramacaoOutputDTO> retirarQualidade(@PathVariable Long id  ) {
        return ResponseEntity.ok(colocarRetirarQualidadeUseCase.alterarStatusQualidade(id, StatusProgramacao.DESABASTECIDO));
    }

    @PatchMapping("/{id}/desabastecer")
    public ResponseEntity<ProgramacaoOutputDTO> desabastecer(@PathVariable Long id) {
        return ResponseEntity.ok(programarUseCase.alterarStatus(id, StatusProgramacao.DESABASTECIDO));
    }
    
}
