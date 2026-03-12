package com.example.mes.producao.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "tb_programacao_equipamento")
@Getter
@Setter
public class Programacao {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

       @OneToOne(cascade = CascadeType.ALL)
        private Lote lote;

       @OneToOne(cascade = CascadeType.ALL)
        private Equipamento equipamento;

        @Enumerated(EnumType.STRING)
        @Column(nullable = false)
        private StatusProgramacao status;

        @Column(nullable = false, name = "data_hora_programada")
        private LocalDateTime dataHoraProgramada;

        private Integer sequenciaFila;

    @Column(precision  =19,  nullable = false )
    private Integer quantidadeConsumida;


    public Programacao(StatusProgramacao statusProgramacao, LocalDateTime dataHoraProgramacao, Integer sequenciaFila, Integer quantidadeConsumida) {
   this.status = statusProgramacao;
   this.dataHoraProgramada = dataHoraProgramacao;
   this.sequenciaFila = sequenciaFila;
   this.quantidadeConsumida = quantidadeConsumida;

    }
}



