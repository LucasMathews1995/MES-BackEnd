package com.example.mes.producao.ordemproducao.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import com.example.mes.producao.lote.domain.Lote;
import com.example.mes.producao.lote.domain.StatusLote;
import com.example.mes.producao.ordemproducao.exceptions.OPNotValidException;

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
    private StatusOP status;

    @Column(nullable = false)
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

    private OrdemProducao(String numeroOP, StatusOP status, LocalDateTime dataCriacao) {
        this.numeroOP = numeroOP;
        this.status = status;
        this.dataCriacao = dataCriacao;
    }

    public static OrdemProducao criarNormal(String numeroOP) {
        return new OrdemProducao(numeroOP, StatusOP.INICIADA, LocalDateTime.now());
    }

    public static OrdemProducao criarRetrabalho(String numeroOP) {
        return new OrdemProducao(numeroOP, StatusOP.RETRABALHO, LocalDateTime.now());
    }

    public void addLote(Lote lote) {

        if (lote.getOrdemProducao() != null) {
            throw new OPNotValidException("O lote " + lote.getNome() + " já está vinculado a uma ordem de produção");

        }
       


        this.lotes.add(lote);

        lote.setOrdemProducao(this);

        if (this.status == StatusOP.INICIADA) {
            this.status = StatusOP.PROCESSANDO;
        }
    }

    public void removeLote(Lote lote) {
        this.lotes.remove(lote);
        lote.setOrdemProducao(null);
    }

    public void desvincularLote(Long idLote) {

        this.lotes.stream()
                .filter(it -> it.getId().equals(idLote) &&
                        (it.getStatus() == StatusLote.QUALIDADE || it.getStatus() == StatusLote.RESERVADO))
                .findFirst().ifPresent(lote -> {
                    removeLote(lote);
                });

        if (this.lotes.isEmpty() && this.getStatus() == StatusOP.PROCESSANDO) {
            this.setStatus(StatusOP.FINALIZADA);
            this.setDataEncerramento(LocalDateTime.now());
        }

    }

    public void validarSePodeSerExcluido() {

        if (this.status != StatusOP.INICIADA && this.status != StatusOP.RETRABALHO) {

            throw new OPNotValidException(
                    "Para excluir, a ordem de produção precisa estar no status INICIADA. Status atual: " + this.status);
        }

        boolean possuiLotesAbastecidos = this.getLotes().stream()
                .anyMatch(it -> it.getStatus() == StatusLote.ABASTECIDO);

        if (possuiLotesAbastecidos) {
            throw new OPNotValidException("Ainda há lotes abastecidos. Precisa desabastecer.");
        }
       
    }

    public void setStatus(StatusOP novoStatus) {
       
        this.status = novoStatus;
    }
}