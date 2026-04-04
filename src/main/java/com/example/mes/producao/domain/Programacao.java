package com.example.mes.producao.domain;

import com.example.mes.producao.api.exception.QuantidadeNotEnoughException;
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
         @JoinColumn(name = "lote_id")
         private Lote lote;

        @ManyToOne( fetch = FetchType.LAZY)
        @JoinColumn(name = "equipamento_id",nullable = false)
        private Equipamento equipamento;

        @Enumerated(EnumType.STRING)
        @Column(nullable = false)
        @Getter @Setter
        private StatusProgramacao status;

        @Column(nullable = false, name = "data_hora_programada")
        private LocalDateTime dataHoraProgramada;

      @Column(nullable = false)
        private Integer fila;

         @Column(precision  =19,  nullable = false )
        private Integer quantidadeConsumida;


    public Programacao(Lote lote, Equipamento equipamento, StatusProgramacao statusProgramacao, Integer quantidadeConsumida) {
        this.equipamento = equipamento;
        this.lote = lote;
        this.status = statusProgramacao;
        this.dataHoraProgramada = LocalDateTime.now();
        this.quantidadeConsumida = quantidadeConsumida;

    }
    public Programacao(){

    }


}



