package com.example.mes.producao.domain;

import com.example.mes.producao.api.exception.AbastecimentoLoteException;
import com.example.mes.producao.api.exception.LoteAbastecidoException;
import com.example.mes.producao.api.exception.NotFoundLoteException;
import com.example.mes.producao.api.exception.OPNotValidException;
import com.example.mes.producao.api.exception.QuantidadeNotEnoughException;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

@Entity(name = "tb_lote")
@Getter
@Setter
public class Lote {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 100, nullable = false, unique = true)
    private String nome;

    @ManyToOne
    @JoinColumn(name = "ordem_producao_id")
    private OrdemProducao ordemProducao;

    @OneToMany(mappedBy = "lote", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Programacao> programacao = new HashSet<>();

    @OneToMany(mappedBy = "lote", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Rastreabilidade> rastreabilidade = new HashSet<>();

    @Column(precision = 19, nullable = false)
    private Integer quantidadeDisponivel;

    private LocalDateTime dataCriacao;
    private String descricao;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private StatusLote status;

    public Lote(Integer quantidadeDisponivel, LocalDateTime dataCriacao, String descricao) {
        this.quantidadeDisponivel = quantidadeDisponivel;
        this.status = StatusLote.DESABASTECIDO;
        this.dataCriacao = dataCriacao;
        this.descricao = descricao;
    }

    public Lote() {
    }

    public void consumirQuantidade(Integer quantidade) {
        if (this.quantidadeDisponivel < quantidade) {
            throw new QuantidadeNotEnoughException("Quantidade maior que a disponível");
        }
        this.quantidadeDisponivel -= quantidade;
    }

    public void retornarQuantidade(Integer quantidade) {

        this.quantidadeDisponivel += quantidade;
    }

    public void removerProgramacao(Programacao programacao) {

        this.programacao.remove(programacao);
        programacao.setLote(null);

    }

    public void adicionarProgramacao(Programacao programacao) {
        this.programacao.add(programacao);
        programacao.setLote(this);
    }

    public void adicionarRastreabilidade(Rastreabilidade rastreabilidade) {
        this.rastreabilidade.add(rastreabilidade);
        rastreabilidade.setLote(this);
    }

    public void removerRastreabilidade(Rastreabilidade rastreabilidade) {

        this.rastreabilidade.remove(rastreabilidade);
        rastreabilidade.setLote(null);
    }

    public void inicializarLote(String nome) {
        this.nome = nome;
        this.status = StatusLote.DESABASTECIDO;
    }

    public void enviarParaQualidade(Integer quantidade) {
        if (this.status == StatusLote.APROVADO || this.status == StatusLote.PRODUZIDO) {
            throw new IllegalStateException(
                    "O lote não pode ser colocado em qualidade pois seu status é de : " + this.status);
        }

        this.status = StatusLote.QUALIDADE;
        retornarQuantidade(quantidade);
    }

    public void retirarDeQualidade(OrdemProducao ordemProducao) {
        if (this.status != StatusLote.QUALIDADE) {
            throw new LoteAbastecidoException(
                    "O lote não pode ser retirado em qualidade pois seu status é de : " + this.status);
        }

        if (ordemProducao.getStatus() != StatusOP.RETRABALHO) {
            throw new OPNotValidException(
                    "O lote só pode ser retirado em qualidade para uma ordem de produção que esteja em retrabalho. Status atual da OP: "
                            + ordemProducao.getStatus());
        }

        this.ordemProducao = ordemProducao;

        this.status = StatusLote.PROGRAMADO;

    }

    public void validarSePodeSerExcluido() {

        if (this.status != StatusLote.DESABASTECIDO) {
            throw new NotFoundLoteException(
                    "Para excluir, o lote precisa estar desabastecido. Status atual: " + this.status);
        }

        boolean possuiProgramacaoAtiva = this.programacao.stream()
                .anyMatch(it -> it.getStatus().equals(StatusProgramacao.PROGRAMADO)
                        || it.getStatus().equals(StatusProgramacao.CRIADO));

        if (possuiProgramacaoAtiva) {
            throw new LoteAbastecidoException("Lote não pode ser excluído pois possui programações ativas.");
        }
    }

    public void programarLote() {
        validarTransicao(this.status, StatusLote.PROGRAMADO);

        setStatus(StatusLote.PROGRAMADO);
    }

    public void vincularAOrdem() {

        if (this.status != StatusLote.DESABASTECIDO) {
            throw new AbastecimentoLoteException(
                    "Não é permitido vincular o lote " + this.nome
                            + " à ordem pois ele não está DESABASTECIDO. Status atual: " + this.status);
        }

        this.status = StatusLote.PROGRAMADO;
    }

    public void abastecerLote() {
        validarTransicao(this.status, StatusLote.ABASTECIDO);

        setStatus(StatusLote.ABASTECIDO);
    }
    public void desabastecerLote() {
        validarTransicao(this.status, StatusLote.DESABASTECIDO);

        setStatus(StatusLote.DESABASTECIDO);
    }
    public void produzirLote() {
        validarTransicao(this.status, StatusLote.PRODUZIDO);

        setStatus(StatusLote.PRODUZIDO);
    }
    public void aprovarLote() {
        validarTransicao(this.status, StatusLote.APROVADO);

        setStatus(StatusLote.APROVADO);
    }

    private void validarTransicao(StatusLote atual, StatusLote novo) {

        boolean permitida = switch (atual) {
            case DESABASTECIDO -> novo == StatusLote.PROGRAMADO || novo == StatusLote.QUALIDADE;
            case PROGRAMADO ->
                novo == StatusLote.ABASTECIDO || novo == StatusLote.QUALIDADE || novo == StatusLote.DESABASTECIDO;
            case ABASTECIDO -> novo == StatusLote.PRODUZIDO || novo == StatusLote.PROGRAMADO;
            case PRODUZIDO -> novo == StatusLote.APROVADO;

            default -> false;
        };

        if (!permitida) {
            throw new AbastecimentoLoteException("Transição não permitida: " + atual + " -> " + novo);
        }
    }

}
