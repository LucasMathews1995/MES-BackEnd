package com.example.mes.cliente.domain;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "usuario")
@Getter
@Setter
public class Cliente {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(length = 100,  nullable = false , unique = true)
    private String nome;

    @Column(length = 100,  nullable = false , unique = true)
    private String empresa;
    @Column(length = 100,  nullable = false , unique = true)
    private String cpnj;
    @Column(length = 100,  nullable = false , unique = true)
    private String email;


}
