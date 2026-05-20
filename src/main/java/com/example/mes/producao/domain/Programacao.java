package com.example.mes.producao.domain;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "tb_programacao", uniqueConstraints = {
    @UniqueConstraint(name = "uk_equipamento_fila", columnNames = {"equipamento_id", "fila"})
})
@Getter
@Setter
public class Programacao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "lote_id")
    private Lote lote;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "equipamento_id", nullable = false)
    private Equipamento equipamento;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Getter
    @Setter
    private StatusProgramacao status;

    @Column(nullable = false, name = "data_hora_programada")
    private LocalDateTime dataHoraProgramada;

    @Column(name = "fila", nullable = true)
    private Integer fila;

    @Column(precision = 19, nullable = false)
    private Integer quantidadeConsumida;

    public Programacao(Lote lote, Equipamento equipamento, StatusProgramacao statusProgramacao,
            Integer quantidadeConsumida) {
        this.equipamento = equipamento;
        this.lote = lote;
        this.status = statusProgramacao;
        this.dataHoraProgramada = LocalDateTime.now();
        this.quantidadeConsumida = quantidadeConsumida;

    }

    public Programacao() {

    }

    public void setFila(Integer novaFila) {
        if (novaFila < 0 )
            throw new IllegalArgumentException("Fila inválida");

        this.fila = novaFila;

    }
    public void colocarEmQualidade() {
        this.fila = null;
        this.status = StatusProgramacao.QUALIDADE;
    }

    

}
