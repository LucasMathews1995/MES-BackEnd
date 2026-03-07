package com.example.mes.producao.domain;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "tb_programacao_equipamento")
public class Programacao {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        @ManyToOne
        @JoinColumn(name = "lote_id", nullable = false)
        private Lote lote;

        @ManyToOne
        @JoinColumn(name = "equipamento_id", nullable = false)
        private Equipamento equipamento;

        @Enumerated(EnumType.STRING)
        @Column(nullable = false)
        private StatusProgramacao status;

        @Column(nullable = false, name = "data_hora_programada")
        private LocalDateTime dataHoraProgramada;

        private Integer sequenciaFila;
    }



