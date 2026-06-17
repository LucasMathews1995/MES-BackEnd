package com.example.mes.producao.programacao.domain;

public enum StatusProgramacao {

    CRIADA,PROGRAMADA,EM_EXECUCAO,CONCLUIDA, CANCELADA;
    

public boolean podeMudarPara(StatusProgramacao novoStatus) {
        return switch (this) {
            case CRIADA -> novoStatus == PROGRAMADA || novoStatus == CANCELADA;
            case PROGRAMADA -> novoStatus == EM_EXECUCAO || novoStatus == CANCELADA;
            case EM_EXECUCAO -> novoStatus == CONCLUIDA || novoStatus == CANCELADA;
            case CONCLUIDA, CANCELADA -> false;
			
        };
}
}