package com.example.mes.producao.rastreabilidade.domain;

import java.time.LocalDateTime;
import com.example.mes.producao.equipamento.model.Equipamento;
import com.example.mes.producao.lote.domain.Lote;
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
    @JoinColumn(name = "equipamento_id", nullable = true)
    private Equipamento equipamento;

    @Column(nullable = false)
    private LocalDateTime dataHora;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private StatusRastreabilidade status;

    private String evento;

    private Rastreabilidade(Lote lote, Equipamento equipamento, LocalDateTime dataHora, StatusRastreabilidade status,
            String evento) {
        this.lote = lote;
        this.equipamento = equipamento;
        this.dataHora = dataHora;
        this.status = status;
        this.evento = evento;
    }

    private Rastreabilidade(Lote lote, LocalDateTime dataHora, StatusRastreabilidade status, String evento) {
        this.lote = lote;
        this.dataHora = dataHora;
        this.status = status;
        this.evento = evento;
    }

    public static Rastreabilidade alterarRastreabilidade(Lote lote, Equipamento equipamento,
            StatusRastreabilidade status) {
        LocalDateTime agora = LocalDateTime.now();

        String descricaoGerada = String.format("O lote %s foi %s no equipamento %s em %s",
                lote.getNome(), status, equipamento.getNome(), agora.toString());

        return new Rastreabilidade(
                lote,
                equipamento,
                agora,
                status,
                descricaoGerada);
    }

    public static Rastreabilidade criarEvento(Lote lote, Equipamento equipamento,
            StatusRastreabilidade status, String evento) {

        return new Rastreabilidade(lote, equipamento, LocalDateTime.now(), status, evento);
    }

   

    public static Rastreabilidade criarEventoLote(StatusRastreabilidade statusRastreabilidade, Lote loteConsumido,
            Lote loteProduzido, Equipamento equipamento) {
        LocalDateTime agora = LocalDateTime.now();

        String descricaoGerada = String.format("O lote %s foi %s em %s no equipamento %s",
                loteConsumido.getNome(), statusRastreabilidade.toString() ,agora.toString(), equipamento.getNome());

        return new Rastreabilidade(
                loteConsumido,
                equipamento,
                agora,
                statusRastreabilidade,
                descricaoGerada);
    }

    public static Rastreabilidade eventoLoteCriado(StatusRastreabilidade statusRastreabilidade, Lote lote) {
        String descricaoGerada = String.format("O lote %s foi criado em: %s ",
                lote.getNome(), lote.getDataCriacao().toString());
        return new Rastreabilidade(
                lote,
                lote.getDataCriacao(),
                statusRastreabilidade,
                descricaoGerada);

    }

    public static Rastreabilidade equipamentoRastreabilidade(Equipamento equipamento,
            StatusRastreabilidade statusRastreabilidade) {

        String descString = String.format("O equipamento %s está %s com capacidade para %d",
                equipamento.getNome(),
                equipamento.getDataAtivacao().toString(),
                equipamento.getCapacidade());

        return new Rastreabilidade(null,equipamento,
                equipamento.getDataAtivacao(),
                statusRastreabilidade,
                descString);

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

    public LocalDateTime getDataHora() {
        return dataHora;
    }

    public void setDataHora(LocalDateTime dataHora) {
        this.dataHora = dataHora;
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
