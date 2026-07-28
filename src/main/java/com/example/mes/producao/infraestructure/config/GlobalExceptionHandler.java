package com.example.mes.producao.infraestructure.config;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import com.example.mes.producao.equipamento.exceptions.EquipamentoNotValidException;
import com.example.mes.producao.equipamento.exceptions.NotFoundEquipamentoException;
import com.example.mes.producao.lote.domain.exceptions.EstrategiaNotFoundException;
import com.example.mes.producao.lote.domain.exceptions.LoteAbastecidoException;
import com.example.mes.producao.lote.domain.exceptions.LoteWithoutOrdemException;
import com.example.mes.producao.lote.domain.exceptions.NotFoundLoteException;
import com.example.mes.producao.ordemproducao.exceptions.OrdemProducaoNotFoundException;
import com.example.mes.producao.ordemproducao.exceptions.OrdemProducaoNotValidException;
import com.example.mes.producao.programacao.domain.exceptions.NotFoundProgramacaoException;
import com.example.mes.producao.programacao.domain.exceptions.ProgramacaoNotValidException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(NotFoundLoteException.class)
    public ResponseEntity<String> handleNotFoundLoteException(NotFoundLoteException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(NotFoundProgramacaoException.class)
    public ResponseEntity<String> handleNotFoundProgramacaoException(NotFoundProgramacaoException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ProgramacaoNotValidException.class)
    public ResponseEntity<String> handleProgramacaoNotValidException(ProgramacaoNotValidException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(EquipamentoNotValidException.class)
    public ResponseEntity<String> handleEquipamentoNotValidException(EquipamentoNotValidException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NotFoundEquipamentoException.class)
    public ResponseEntity<String> handleNotFoundEquipamentoException(NotFoundEquipamentoException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
    }
      @ExceptionHandler(LoteAbastecidoException.class)
    public ResponseEntity<String> handleLoteAbastecidoException(LoteAbastecidoException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(OrdemProducaoNotFoundException.class)
    public ResponseEntity<String> handleOrdemProducaoNotFoundException(OrdemProducaoNotFoundException ex){
        return new  ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
    }
    @ExceptionHandler(OrdemProducaoNotValidException.class)
    public ResponseEntity<String> handleOrdemProducaoNotValidException(OrdemProducaoNotValidException ex){
         return new  ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(EstrategiaNotFoundException.class)
    public ResponseEntity<String> handleEstrategiaNotFoundException(EstrategiaNotFoundException ex){
         return new  ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(LoteWithoutOrdemException.class)
    public ResponseEntity<String> handleEstrategiaNotFoundException(LoteWithoutOrdemException ex){
         return new  ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }
    
}
