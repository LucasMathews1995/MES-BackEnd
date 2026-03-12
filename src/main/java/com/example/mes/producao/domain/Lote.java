package com.example.mes.producao.domain;

import com.example.mes.producao.api.exception.QuantidadeNotEnoughException;
import jakarta.persistence.*;
import lombok.*;


import java.time.LocalDateTime;



@Entity(name= "tb_lote")
@Getter
@Setter
public class Lote {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 100,  nullable = false , unique = true)
    private String loteNome;

    @ManyToOne
    @JoinColumn(name = "ordem_producao_id")
    private OrdemProducao ordemProducao;



    @Column(precision  =19,  nullable = false )
    private Integer quantidadeDisponivel;




    private LocalDateTime dataCriacao;
    private String descricao;



   @OneToOne(cascade = CascadeType.ALL)
    private Programacao programacao;

    public Lote( Integer quantidadeDisponivel, LocalDateTime dataCriacao, String descricao) {
        this.quantidadeDisponivel = quantidadeDisponivel;

        this.dataCriacao = dataCriacao;
        this.descricao = descricao;
    }
    public Lote() {}



    public void consumirQuantidade(Integer quantidade) {
        if(this.quantidadeDisponivel < quantidade) {
            throw new QuantidadeNotEnoughException("Quantidade maior que a disponível");
        }
        this.quantidadeDisponivel -= quantidade;
    }




}
