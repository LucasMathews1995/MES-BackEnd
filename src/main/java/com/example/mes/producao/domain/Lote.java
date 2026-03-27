package com.example.mes.producao.domain;

import com.example.mes.producao.api.exception.QuantidadeNotEnoughException;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Generated;
import org.hibernate.generator.EventType;


import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;


@Entity(name= "tb_lote")
@Getter
@Setter
public class Lote {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 100,  nullable = false , unique = true)
    private String nome;

    @ManyToOne
    @JoinColumn(name = "ordem_producao_id")
    private OrdemProducao ordemProducao;

    @OneToMany(mappedBy= "lote" ,cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Programacao> programacao =  new HashSet<>();

    @Column(precision  =19,  nullable = false )
    private Integer quantidadeDisponivel;




    private LocalDateTime dataCriacao;
    private String descricao;


    @Column(columnDefinition = "serial", insertable = false)
    @Generated(event = EventType.INSERT)
    private Integer sequencia;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private StatusLote status;


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
