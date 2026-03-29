package com.example.mes.producao.api.exception;

public class AlreadyExistOrdemProducaoException extends RuntimeException {
    public AlreadyExistOrdemProducaoException(String message) {
        super(message);
    }
}
