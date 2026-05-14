package com.example.mes.producao.domain;

import java.time.LocalDateTime;

import org.hibernate.validator.constraints.UniqueElements;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "tb_rastreabilidade")
public class Rastreabilidade {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "lote_id", nullable = false)
    private Lote lote;

    @ManyToOne
    @JoinColumn(name = "equipamento_id", nullable = false)
    private Equipamento equipamento;

    @Column(nullable = false)
    private LocalDateTime dataHoraEntrada;

    @Column(nullable = true)
    private LocalDateTime dataHoraSaida;

    @Column( nullable = false)
    @Enumerated(EnumType.STRING)
    private StatusRastreabilidade status;
    
    private String evento;

    public Rastreabilidade(Lote lote, Equipamento equipamento, LocalDateTime dataHoraEntrada, StatusRastreabilidade status, String evento) {
        this.lote = lote;
        this.equipamento = equipamento;
        this.dataHoraEntrada = dataHoraEntrada;
        this.status = status;
        this.evento = evento;
    }
    public Rastreabilidade() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Lote getLote() {
        return lote;
    }

    public void setLote(Lote lote) {
        this.lote = lote;
    }

    public Equipamento getEquipamento() {
        return equipamento;
    }

    public void setEquipamento(Equipamento equipamento) {
        this.equipamento = equipamento;
    }

    public LocalDateTime getDataHoraEntrada() {
        return dataHoraEntrada;
    }

    public void setDataHoraEntrada(LocalDateTime dataHoraEntrada) {
        this.dataHoraEntrada = dataHoraEntrada;
    }

    public LocalDateTime getDataHoraSaida() {
        return dataHoraSaida;
    }

    public void setDataHoraSaida(LocalDateTime dataHoraSaida) {
        this.dataHoraSaida = dataHoraSaida;
    }

    public StatusRastreabilidade getStatus() {
        return status;
    }
    public void setStatus(StatusRastreabilidade status) {
        this.status = status;
    }
    public String getEvento() {
        return evento;
    }
    public void setEvento(String evento) {
        this.evento = evento;
    }   


}
