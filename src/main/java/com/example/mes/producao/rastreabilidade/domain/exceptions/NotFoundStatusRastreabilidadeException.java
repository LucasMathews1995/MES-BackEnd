package com.example.mes.producao.rastreabilidade.domain.exceptions;

public class NotFoundStatusRastreabilidadeException extends RuntimeException {
    public NotFoundStatusRastreabilidadeException(String message){
        super(message);
    }
}
