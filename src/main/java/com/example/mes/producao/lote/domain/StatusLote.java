package com.example.mes.producao.lote.domain;

public enum StatusLote {
  QUALIDADE , DESABASTECIDO,RESERVADO,ABASTECIDO,CONSUMIDO,PRODUZINDO,DECISAO;

    public boolean podeMudarPara(StatusLote novoStatus) {
        return switch (this) {
            case QUALIDADE -> novoStatus == DESABASTECIDO|| novoStatus == ABASTECIDO;
            case DESABASTECIDO -> novoStatus == RESERVADO || novoStatus == QUALIDADE;
            case RESERVADO -> novoStatus == ABASTECIDO || novoStatus == DESABASTECIDO || novoStatus == QUALIDADE;
            case ABASTECIDO    -> novoStatus == CONSUMIDO ||    novoStatus == DESABASTECIDO || novoStatus == QUALIDADE;
            case PRODUZINDO -> novoStatus == CONSUMIDO || novoStatus == DESABASTECIDO || novoStatus == QUALIDADE || novoStatus == ABASTECIDO;
            case CONSUMIDO -> false; 
            case DECISAO -> false;
        };
    }
}
