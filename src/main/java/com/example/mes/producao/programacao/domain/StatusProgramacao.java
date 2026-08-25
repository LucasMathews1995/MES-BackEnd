package com.example.mes.producao.programacao.domain;

public enum StatusProgramacao {

    CRIADA,PROGRAMADA,EM_EXECUCAO,CONCLUIDA, CANCELADA, QUALIDADE, DESABASTECIDO;
    

public boolean podeMudarPara(StatusProgramacao novoStatus) {
        return switch (this) {
            case CRIADA -> novoStatus == PROGRAMADA || novoStatus == CANCELADA;
            case PROGRAMADA -> novoStatus == EM_EXECUCAO || novoStatus == CANCELADA || novoStatus == QUALIDADE;
            case EM_EXECUCAO -> novoStatus == CONCLUIDA || novoStatus == CANCELADA || novoStatus == DESABASTECIDO;
            case QUALIDADE -> novoStatus == CRIADA;
            case DESABASTECIDO -> novoStatus == QUALIDADE || novoStatus == PROGRAMADA;
            case CONCLUIDA, CANCELADA-> false;
    
			
        };
}
}