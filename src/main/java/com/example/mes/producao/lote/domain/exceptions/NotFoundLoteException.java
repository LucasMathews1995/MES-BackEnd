package com.example.mes.producao.lote.domain.exceptions;

public class NotFoundLoteException extends RuntimeException {
    public NotFoundLoteException(String message) {
        super(message);
    }

}
