package com.example.mes.producao.domain;


import jakarta.persistence.*;
import lombok.Data;


import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;


@Entity
@Table(name = "tb_ordem_producao")
@Data
public class OrdemProducao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 100,  nullable = false , unique = true)
    private String numeroOP;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatusOP status;

    @Column(nullable = false , name  = "data_criação")
    private LocalDateTime dataCriacao;

    @Column(name = "data_encerramento")
    private LocalDateTime dataEncerramento;

    @OneToMany(mappedBy = "ordemProducao")
    private Set<Lote> lotes = new HashSet<>();









}
