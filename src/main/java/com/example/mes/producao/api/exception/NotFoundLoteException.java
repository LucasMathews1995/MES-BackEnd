package com.example.mes.producao.api.exception;

public class NotFoundLoteException extends RuntimeException {
    public NotFoundLoteException(String message) {
        super(message);
    }
}
