package com.example.mes.producao.domain;

import com.example.mes.producao.api.exception.DateTimeEquipamentoException;
import jakarta.persistence.*;
import lombok.*;


import java.time.LocalDateTime;
import java.util.HashSet;

import java.util.Set;





@Entity(name = "tb_equipamento")
@Getter
@Setter
@ToString(exclude = "programacao")
@EqualsAndHashCode(exclude = "programacao")
public class Equipamento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false,  length = 50)
    private String nome;


    private String sigla;


    @OneToOne(cascade = CascadeType.ALL)
    private Programacao programacao ;

    @Column(nullable = false,  name= "ativo")
    private boolean isAtivo;

    private String descricao;

    private LocalDateTime dataAtivacao;

    private LocalDateTime dataParado;


    public Equipamento(String nome, String sigla , String descricao, LocalDateTime dataAtivacao ) {
        this.nome = nome;
        this.sigla = sigla;
        this.descricao = descricao;
        this.dataAtivacao = dataAtivacao;
        this.dataParado = null;
        isAtivo = true;
    }
    public Equipamento(){

    }









}
