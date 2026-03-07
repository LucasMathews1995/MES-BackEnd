package com.example.mes.producao.domain;

import jakarta.persistence.*;
import lombok.Data;


import java.util.HashSet;

import java.util.Set;


@Entity
@Table(name= "tb_equipamento")
@Data
public class Equipamento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false,  length = 50)
    private String nome;


    @OneToMany(mappedBy =  "equipamento", fetch = FetchType.LAZY)
    private Set<Programacao> programacoes =  new HashSet<>();

    @Column(nullable = false,  name= "ativo")
    private boolean isAtivo;
    private String descricao;

}
