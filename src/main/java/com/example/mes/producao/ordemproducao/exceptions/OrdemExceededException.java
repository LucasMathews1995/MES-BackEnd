package com.example.mes.producao.ordemproducao.exceptions;

public class OrdemExceededException extends RuntimeException {

    public OrdemExceededException(String message ){
        super(message);
    }

}
