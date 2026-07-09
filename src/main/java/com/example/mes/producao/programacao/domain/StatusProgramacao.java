package com.example.mes.producao.programacao.domain;

public enum StatusProgramacao {

    CRIADA,PROGRAMADA,EM_EXECUCAO,CONCLUIDA, CANCELADA, QUALIDADE;
    

public boolean podeMudarPara(StatusProgramacao novoStatus) {
        return switch (this) {
            case CRIADA -> novoStatus == PROGRAMADA || novoStatus == CANCELADA;
            case PROGRAMADA -> novoStatus == EM_EXECUCAO || novoStatus == CANCELADA || novoStatus == QUALIDADE;
            case EM_EXECUCAO -> novoStatus == CONCLUIDA || novoStatus == CANCELADA;
            case QUALIDADE -> novoStatus == CRIADA ;
            case CONCLUIDA, CANCELADA-> false;
			
        };
}
}