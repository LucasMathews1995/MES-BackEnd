package com.example.mes.producao.domain;

import java.time.LocalDateTime;
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
    @JoinColumn(name = "lote_id", nullable = true)
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

    private Rastreabilidade(Lote lote, Equipamento equipamento, LocalDateTime dataHoraEntrada, StatusRastreabilidade status, String evento) {
        this.lote = lote;
        this.equipamento = equipamento;
        this.dataHoraEntrada = dataHoraEntrada;
        this.status = status;
        this.evento = evento;
    }

    public static Rastreabilidade alterarRastreabilidade(Lote lote, Equipamento equipamento,StatusRastreabilidade status) {
        LocalDateTime agora = LocalDateTime.now();

        String descricaoGerada = String.format("O lote %s foi %s no equipamento %s em %s",
                lote.getNome(), status, equipamento.getNome(), agora.toString());

        return new Rastreabilidade(
                lote, 
                equipamento, 
                agora, 
                status, 
                descricaoGerada
        );
    }


    public static Rastreabilidade criarEvento(Lote lote, Equipamento equipamento, 
                                              StatusRastreabilidade status, String evento) {
     
        return new Rastreabilidade(lote, equipamento, LocalDateTime.now(), status, evento);
    }

    public static Rastreabilidade criarEventoEquipamento(Equipamento equipamento, String evento) {
        LocalDateTime agora = LocalDateTime.now();

        String descricaoGerada = String.format("O equipamento %s teve o evento: %s em %s",
                equipamento.getNome(), evento, agora.toString());

        return new Rastreabilidade(
                null, 
                equipamento, 
                agora, 
                StatusRastreabilidade.CRIADO, 
                descricaoGerada
        );
    }

    public static Rastreabilidade criarEventoLote(Lote lote, Equipamento equipamento, String evento) {
        LocalDateTime agora = LocalDateTime.now();

        String descricaoGerada = String.format("O lote %s teve o evento: %s em %s",
                lote.getNome(), evento, agora.toString());

        return new Rastreabilidade(
                lote, 
                equipamento, 
                agora, 
                StatusRastreabilidade.CRIADO, 
                descricaoGerada
        );
    }



    protected Rastreabilidade() {
    }

    public void registrarSaida() {
        this.dataHoraSaida = LocalDateTime.now();
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
