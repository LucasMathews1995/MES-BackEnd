package com.example.mes.producao.programacao.domain.exceptions;

public class NotFoundProgramacaoException extends RuntimeException {
    public NotFoundProgramacaoException(String message) {
        super(message);
    }
}