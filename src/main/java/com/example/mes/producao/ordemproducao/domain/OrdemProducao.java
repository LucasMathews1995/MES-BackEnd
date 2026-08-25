package com.example.mes.producao.ordemproducao.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import com.example.mes.producao.equipamento.model.Equipamento;
import com.example.mes.producao.lote.domain.Lote;
import com.example.mes.producao.lote.domain.StatusLote;
import com.example.mes.producao.ordemproducao.exceptions.OrdemProducaoNotValidException;

import com.example.mes.producao.ordemproducao.exceptions.OrdemExceededException;

@Entity
@Table(name = "tb_ordem_producao")
public class OrdemProducao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter
    private Long id;

    @Column(name = "ordem_venda_id")
    @Getter
    private Long ordemVendaId;

    @Column(length = 100, nullable = false, unique = true, name = "numero_op")
    @Getter
    @Setter
    private String numeroOP;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "equipamento_id", nullable = false)
    @Getter
    private Equipamento equipamento;

    @OneToMany(mappedBy = "ordemProducao", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Lote> lotes = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Getter
    private StatusOP status;

    @Column(nullable = false)
    @Getter
    @Setter
    private LocalDateTime dataCriacao;

    @Column(name = "data_encerramento")
    @Getter
    @Setter
    private LocalDateTime dataEncerramento;

    @Column(name = "capacidade_ocupada", nullable = false)
    @Getter
    private Long capacidadeOcupada;

    @Column(name = "capacidade_maxima", nullable = false)
    @Getter
    @Setter
    private long capacidadeMaxima;

    private OrdemProducao(Equipamento equipamento, String numeroOP, StatusOP status, LocalDateTime dataCriacao,
            Long capacidadeMaxima) {
        this.equipamento = equipamento;
        this.numeroOP = numeroOP;
        this.status = status;
        this.dataCriacao = dataCriacao;

        this.capacidadeMaxima = capacidadeMaxima;
        this.capacidadeOcupada = 0L;

    }

    public OrdemProducao() {
    }

    public static OrdemProducao criarNormal(Equipamento equipamento, String numeroOP, Long capacidadeMaxima) {
        return new OrdemProducao(equipamento, numeroOP, StatusOP.INICIADA, LocalDateTime.now(), capacidadeMaxima);
    }

    public static OrdemProducao criarRetrabalho(Equipamento equipamento, String numeroOP, Long capacidadeMaxima) {
        return new OrdemProducao(equipamento, numeroOP, StatusOP.RETRABALHO, LocalDateTime.now(), capacidadeMaxima);
    }

    public void adicionarLote(Lote lote) {

        if (this.capacidadeOcupada + lote.getQuantidadeDisponivel() > this.capacidadeMaxima) {
            throw new OrdemProducaoNotValidException(
                    "A ordem de produção não possui capacidade suficiente para adicionar o lote " + lote.getNome());

        }
        this.capacidadeOcupada = getCapacidadeOcupada() + lote.getQuantidadeDisponivel();
        this.lotes.add(lote);
        lote.setOrdemProducao(this);
    }

    public void finalizarLoteNaOP(Lote lote) {
        if (lote.getStatus() != StatusLote.ABASTECIDO && lote.getStatus() != StatusLote.CONSUMIDO) {
            throw new OrdemProducaoNotValidException(
                    "Impossível remover lote que não está abastecido ou consumido da ordem de produção");
        }

        if (this.status == StatusOP.PROCESSANDO) {
            this.status = StatusOP.FINALIZADA;
            this.dataEncerramento = LocalDateTime.now();
        }

    }

    public void setCapacidadeMaxima(Long capacidade) {
        if (!status.equals(StatusOP.INICIADA) && !status.equals(StatusOP.RETRABALHO)) {
            throw new OrdemProducaoNotValidException("a ordem não permite pois a ordem já está em processamento");

        }
        this.capacidadeMaxima = capacidade;
    }

    public void processar() {

        if (this.status.equals(StatusOP.INICIADA) || this.status.equals(StatusOP.RETRABALHO)) {
            this.status = StatusOP.PROCESSANDO;
        } else {
            throw new OrdemProducaoNotValidException(
                    "Não é possível iniciar o processamento de uma OP com status " + this.status);
        }
    }

    public void pausarOP(Equipamento equipamento) {
        if (equipamento.isAtivo()) {
            throw new OrdemProducaoNotValidException(
                    "Impossível pausar a ordem de produção enquanto o equipamento estiver ativo");
        }

        this.status = StatusOP.PAUSADA;

    }

    public void validarSePodeSerExcluido() {

        if (this.status != StatusOP.INICIADA && this.status != StatusOP.RETRABALHO) {

            throw new OrdemProducaoNotValidException(
                    "Para excluir, a ordem de produção precisa estar no status INICIADA ou RETRABALHO. Status atual: "
                            + this.status);
        }

    }

    public void setStatus(StatusOP novoStatus) {

        this.status = novoStatus;
    }

    public void trocarEquipamento(Equipamento equipamento) {
        if (equipamento == null) {
            throw new IllegalArgumentException("O equipamento não pode ser nulo.");
        }

        long quantidadeJaAlocada = 0L;

        if (quantidadeJaAlocada > equipamento.getCapacidade()) {
            throw new OrdemProducaoNotValidException(
                    String.format(
                            "Não é possível trocar o equipamento. A quantidade já alocada nos lotes (%d) supera a capacidade do novo equipamento (%d).",
                            quantidadeJaAlocada, equipamento.getCapacidade()));

        }

        this.capacidadeMaxima = equipamento.getCapacidade();
    }

    public void setCapacidadeOcupada(Long capacidadeOcupada) {
        this.capacidadeOcupada = capacidadeOcupada;
    }

    public void adicionarListadeLotes(List<Lote> novosLotes) {

        if (novosLotes == null || novosLotes.isEmpty()) {
            throw new IllegalArgumentException("A lista de lotes não pode ser vazia.");
        }

        Long peso = novosLotes.stream().mapToLong(Lote::getQuantidadeDisponivel).sum();

        Long quantidadeDisponivel = capacidadeMaxima - capacidadeOcupada;

        if (peso > quantidadeDisponivel) {
            throw new OrdemExceededException("Valor do peso dos lotes é maior que a capacidade maxima da OP");
        }

    }

}