package com.example.mes.producao.api.exception;



import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(DateTimeEquipamentoException.class)
    public ResponseEntity<?> handleDateTimeException(DateTimeEquipamentoException ex){

        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("message", ex.getMessage());
        body.put("status", HttpStatus.CONFLICT.value());

        return new ResponseEntity<>(body, HttpStatus.CONFLICT);
    }
    @ExceptionHandler(AlreadyExistsEquipamentoException.class)
    public ResponseEntity<String> handleAlreadyExistsException(AlreadyExistsEquipamentoException ex){
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(LoteBoundsException.class)
    public ResponseEntity<String> handleLoteBoundsException(LoteBoundsException ex){
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.CONFLICT);
    }
    @ExceptionHandler(NotFoundEquipamentoException.class)
    public ResponseEntity<String> handleNotFoundException(NotFoundEquipamentoException ex){
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
    }
    @ExceptionHandler(NotFoundLoteException.class)
    public ResponseEntity<String> handleNotFoundException(NotFoundLoteException ex){
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
    }
    @ExceptionHandler(QuantidadeNotEnoughException.class)
    public ResponseEntity<String> handleNotEnoughException(QuantidadeNotEnoughException ex){
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(ProgramacaoNotFoundException.class)
    public ResponseEntity<String> handleNotFoundException(ProgramacaoNotFoundException ex){
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
    }
    @ExceptionHandler(OrdemProducaoNotFoundException.class)
    public ResponseEntity<String> handleNotFoundException(OrdemProducaoNotFoundException ex){
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
    }
    @ExceptionHandler(OrdemAndLoteException.class)
    public ResponseEntity<String> handleNotFoundException(OrdemAndLoteException ex){
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
    }
    @ExceptionHandler(AlreadyExistOrdemProducaoException.class)
    public ResponseEntity<String> AlreadyExistOrdemProducaoException(AlreadyExistOrdemProducaoException ex){
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.CONFLICT);
    }
    @ExceptionHandler(QualityException.class)
    public ResponseEntity<String> QualityException(QualityException ex){
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.CONFLICT);
    }



}
