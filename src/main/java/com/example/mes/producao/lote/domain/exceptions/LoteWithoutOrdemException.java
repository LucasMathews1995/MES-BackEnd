package com.example.mes.producao.lote.domain.exceptions;

public class LoteWithoutOrdemException extends RuntimeException {
    public LoteWithoutOrdemException(String messsage ){
        super(messsage);
    }
}