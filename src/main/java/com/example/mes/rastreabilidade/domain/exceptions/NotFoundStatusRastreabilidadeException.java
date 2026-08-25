package com.example.mes.rastreabilidade.domain.exceptions;

public class NotFoundStatusRastreabilidadeException extends RuntimeException {
    public NotFoundStatusRastreabilidadeException(String message){
        super(message);
    }
}
