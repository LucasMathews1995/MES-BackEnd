package com.example.mes.producao.domain;

import com.example.mes.producao.api.exception.DateTimeEquipamentoException;
import jakarta.persistence.*;
import lombok.*;


import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;

import java.util.List;
import java.util.Set;





@Entity(name = "tb_equipamento")
@Getter
@Setter
@ToString()
@EqualsAndHashCode()
public class Equipamento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false,  length = 50)
    private String nome;


    private String sigla;


    @OneToMany(mappedBy = "equipamento", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Programacao> programacao = new HashSet<>();

    @Column(nullable = false,  name= "ativo")
    private boolean isAtivo;

    private String descricao;

    private LocalDateTime dataAtivacao;

    private LocalDateTime dataParado;

    @Enumerated(EnumType.STRING)
    private StatusEquipamento statusEquipamento;


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

    public void adicionarProgramacao(Programacao programacao){
        this.programacao.add(programacao);
        programacao.setEquipamento(this);

    }
    public void removerProgramacao(Programacao programacao){
        this.programacao.remove(programacao);
        programacao.setEquipamento(null);
    }








}
