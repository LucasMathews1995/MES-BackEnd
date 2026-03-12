package com.example.mes.producao.api.exception;

public class QuantidadeNotEnoughException extends RuntimeException {
    public QuantidadeNotEnoughException(String message) {
        super(message);
    }
}
