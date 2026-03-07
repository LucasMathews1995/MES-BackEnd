package com.example.mes.producao.domain;

import jakarta.persistence.*;
import lombok.Data;


import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;


@Entity
@Table(name= "tb_lote")
@Data
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
    private Integer quantidadeEstoque;


    private LocalDateTime dataCriacao;
    private String descricao;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "lote")
    private List<Programacao> programacao =  new ArrayList<>();

}
