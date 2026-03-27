package com.example.mes.producao.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Generated;
import org.hibernate.generator.EventType;

import java.time.LocalDateTime;

@Entity
@Table(name = "tb_programacao")
@Getter
@Setter
public class Programacao {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

         @ManyToOne
         @JoinColumn(name = "lote_id", nullable = false)
         private Lote lote;

        @ManyToOne( fetch = FetchType.LAZY)
        @JoinColumn(name = "equipamento_id")
        private Equipamento equipamento;

        @Enumerated(EnumType.STRING)
        @Column(nullable = false)
        @Getter @Setter
        private StatusProgramacao status;

        @Column(nullable = false, name = "data_hora_programada")
        private LocalDateTime dataHoraProgramada;

        @Column(columnDefinition = "serial", insertable = false)
        @Generated(event = EventType.INSERT)
        private Integer fila;

         @Column(precision  =19,  nullable = false )
        private Integer quantidadeConsumida;


    public Programacao(StatusProgramacao statusProgramacao,  Integer quantidadeConsumida) {
   this.status = statusProgramacao;


   this.quantidadeConsumida = quantidadeConsumida;

    }
    public Programacao(){

    }


}



