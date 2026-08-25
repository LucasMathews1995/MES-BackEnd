package com.example.mes.rastreabilidade.domain;

public enum StatusRastreabilidade {
    
    REJEITADO(CategoriaRastreabilidade.QUALIDADE),
    APROVADO(CategoriaRastreabilidade.QUALIDADE),
    CONSUMIDO(CategoriaRastreabilidade.LOTE),
    CRIADO_LOTE(CategoriaRastreabilidade.LOTE),

    PARADA(CategoriaRastreabilidade.EQUIPAMENTO),
    OPERANDO(CategoriaRastreabilidade.EQUIPAMENTO),

    DESABASTECIDO(CategoriaRastreabilidade.PROGRAMACAO),
    ABASTECIDO(CategoriaRastreabilidade.PROGRAMACAO),

  
    CRIADO(CategoriaRastreabilidade.PROGRAMACAO),
    PROGRAMADO(CategoriaRastreabilidade.PROGRAMACAO),
    EM_EXECUCAO(CategoriaRastreabilidade.PROGRAMACAO),
    CONCLUIDO(CategoriaRastreabilidade.PROGRAMACAO);

    private final CategoriaRastreabilidade categoria;


    StatusRastreabilidade(CategoriaRastreabilidade categoria) {
        this.categoria = categoria;
    }

    public CategoriaRastreabilidade getCategoria() {
        return categoria;
    }

    
    public boolean pertenceA(CategoriaRastreabilidade categoriaEsperada) {
        return this.categoria == categoriaEsperada;
    }
}