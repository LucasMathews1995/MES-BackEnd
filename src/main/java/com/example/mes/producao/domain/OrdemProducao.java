package com.example.mes.producao.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import com.example.mes.producao.api.exception.OrdemAndLoteException;

@Entity
@Table(name = "tb_ordem_producao")
public class OrdemProducao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter
    private Long id;

    @Setter
    @Getter
    private Long ordemVendaId;

    @Column(length = 100, nullable = false, unique = true, name = "numero_op")
    @Getter
    @Setter
    private String numeroOP;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Getter
    @Setter
    private StatusOP status;

    @Column(nullable = false, name = "data_criacao")
    @Getter
    @Setter
    private LocalDateTime dataCriacao;

    @Column(name = "data_encerramento")
    @Getter
    @Setter
    private LocalDateTime dataEncerramento;

    @OneToMany(mappedBy = "ordemProducao")
    @Getter
    private Set<Lote> lotes = new HashSet<>();

    public OrdemProducao() {

    }

    public OrdemProducao(String numeroOP) {
        this.numeroOP = numeroOP;
        this.status = StatusOP.INICIADA;
        this.dataCriacao = LocalDateTime.now();
        this.dataEncerramento = null;

    }

    public void addLote(Lote lote) {
        this.lotes.add(lote);
        lote.setOrdemProducao(this);
    }

    public void removeLote(Lote lote) {
        this.lotes.remove(lote);
        lote.setOrdemProducao(null);
    }

    public void desvincularLote(Long idLote) {

        this.lotes.stream()
                .filter(it -> it.getId().equals(idLote) &&
                        (it.getStatus() == StatusLote.QUALIDADE || it.getStatus() == StatusLote.APROVADO))
                .findFirst().ifPresent(lote -> {
                    removeLote(lote);
                });

        if (this.lotes.isEmpty() && this.getStatus() == StatusOP.PROCESSANDO) {
            this.setStatus(StatusOP.FINALIZADA);
            this.setDataEncerramento(LocalDateTime.now());
        }

    }

    public void validarSePodeSerExcluido() {

        if (this.status != StatusOP.INICIADA) {
            throw new OrdemAndLoteException(
                    "Para excluir, a ordem de produção precisa estar no status INICIADA. Status atual: " + this.status);
        }

        boolean possuiLotesAbastecidos = this.getLotes().stream()
                .anyMatch(it -> it.getStatus() == StatusLote.ABASTECIDO);

        if (possuiLotesAbastecidos) {
            throw new OrdemAndLoteException("Ainda há lotes abastecidos. Precisa desabastecer.");
        }

    }
}