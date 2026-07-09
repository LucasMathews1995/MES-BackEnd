package com.example.mes.producao.lote.domain;

import com.example.mes.producao.lote.domain.exceptions.LoteAbastecidoException;
import com.example.mes.producao.lote.domain.exceptions.NotFoundLoteException;
import com.example.mes.producao.ordemproducao.domain.OrdemProducao;
import com.example.mes.producao.ordemproducao.exceptions.OPNotValidException;
import com.example.mes.producao.programacao.domain.Programacao;
import com.example.mes.producao.programacao.domain.StatusProgramacao;
import com.example.mes.producao.rastreabilidade.domain.Rastreabilidade;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lote_pai_id")
    private Lote lotePai;

    @OneToMany(mappedBy = "loteConsumido", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Programacao> programacao = new ArrayList<>();

    @OneToMany(mappedBy = "lote", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Rastreabilidade> rastreabilidade = new HashSet<>();

    @Column(precision = 7, nullable = false)
    private Long quantidadeDisponivel;

    private LocalDateTime dataCriacao;
    private String descricao;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private StatusLote status;

    private Lote(String nome, Long quantidadeDisponivel, LocalDateTime dataCriacao, String descricao) {
        this.nome = nome;
        this.quantidadeDisponivel = quantidadeDisponivel;
        this.status = StatusLote.DESABASTECIDO;
        this.dataCriacao = dataCriacao;
        this.descricao = descricao;
    }

    public Lote() {
    }

    public void reservarLote(Long quantidadeConsumida) {
        if (this.status == StatusLote.ABASTECIDO || this.status == StatusLote.CONSUMIDO
                || this.status == StatusLote.RESERVADO || this.status == StatusLote.PRODUZINDO) {
            throw new LoteAbastecidoException("O lote já está abastecido ou consumido.");
        }
        if (this.ordemProducao == null) {
            throw new OPNotValidException("O lote deve estar associado a uma ordem de  produção para ser abastecido.");
        }

        this.status = StatusLote.RESERVADO;

    }

    public void desabastecerLote() {
        if (this.status != StatusLote.ABASTECIDO && this.status != StatusLote.RESERVADO) {
            throw new LoteAbastecidoException("O lote " + this.nome + " não está abastecido.");
        }
        if (programacao == null) {
            throw new NotFoundLoteException("Programação associada ao lote não encontrada.");
        }
        if (programacao.getFirst().getStatus() == StatusProgramacao.CONCLUIDA) {
            throw new LoteAbastecidoException(
                    "A programação associada ao lote  está concluída, portanto o lote não pode ser desabastecido.");
        }

        this.status = StatusLote.DESABASTECIDO;
    }

    public void liberarLote() {
        if (this.status != StatusLote.PRODUZINDO) {
            throw new LoteAbastecidoException("O lote " + this.nome + " não está em produção.");
        }
        this.status = StatusLote.DESABASTECIDO;
    }

    public void rejeitarLoteConsumido(Long quantidade) {
        if (this.status == StatusLote.CONSUMIDO ) {
            throw new LoteAbastecidoException("O lote " + this.nome + " está consumido.");
        }

        this.quantidadeDisponivel += quantidade;
        this.status = StatusLote.QUALIDADE;

    }

    public void abastecerLote() {
        if (this.status != StatusLote.RESERVADO) {
            throw new LoteAbastecidoException("O lote " + this.nome + " não está reservado.");
        }
        if (this.ordemProducao == null) {
            throw new OPNotValidException("O lote deve estar associado a uma ordem de  produção para ser abastecido.");

        }

        this.status = StatusLote.ABASTECIDO;

    }

    public void consumirLote() {
        if (this.getQuantidadeDisponivel() != 0) {

            this.status = StatusLote.DESABASTECIDO;
        } else {
            this.status = StatusLote.CONSUMIDO;
            this.ordemProducao.finalizarLoteNaOP(this);
        }
    }

    public void setLotePai(Lote lotePai) {
        this.lotePai = lotePai;
    }

    public Lote vincularOP(OrdemProducao ordemProducao) {

        ordemProducao.adicionarLote(this);
        this.setOrdemProducao(ordemProducao);
        return this;
    }

    public Lote gerarFilhoParaProgramacao(OrdemProducao producao, Long quantidadeDesejada, String nome) {

        if (quantidadeDesejada <= 0) {
            throw new LoteAbastecidoException("A quantidade a ser consumida deve ser maior que zero.");
        }
        if (quantidadeDesejada > this.quantidadeDisponivel) {
            throw new LoteAbastecidoException(
                    "Saldo insuficiente no lote " + this.getNome() +
                            ". Disponível: " + this.getQuantidadeDisponivel());

        }

        long novaQuantidadePai = this.getQuantidadeDisponivel() - quantidadeDesejada;
        this.setQuantidadeDisponivel(novaQuantidadePai);

        Lote loteFilho = new Lote();
        loteFilho.setNome(nome);
        loteFilho.setQuantidadeDisponivel(quantidadeDesejada);
        loteFilho.setDataCriacao(LocalDateTime.now());
        loteFilho.setDescricao("Produzido a partir do lote pai: " + this.getNome());
        loteFilho.setLotePai(this);
        loteFilho.setStatus(StatusLote.DESABASTECIDO);

        return loteFilho;
    }

    public void retornarQuantidade(Long quantidade) {
        if (quantidade <= 0) {
            throw new LoteAbastecidoException("A quantidade a ser retornada deve ser maior que zero.");
        }
        this.quantidadeDisponivel += quantidade;
    }

    public void produzirLote() {
        if (this.status == null || !StatusLote.DESABASTECIDO.equals(this.status)) {
            throw new LoteAbastecidoException("O lote " + this.nome + " não está em um estado válido para produção.");
        }
        this.status = StatusLote.PRODUZINDO;
    }

    public void consumirQuantidade(Long quantidade) {
        if (quantidade <= 0) {
            throw new LoteAbastecidoException("A quantidade a ser consumida deve ser maior que zero.");
        }
        if (quantidade > this.quantidadeDisponivel) {
            throw new LoteAbastecidoException(
                    "Saldo insuficiente no lote " + this.getNome() +
                            ". Disponível: " + this.getQuantidadeDisponivel());
        }
        this.quantidadeDisponivel -= quantidade;
    }

    public Lote rejeitarLoteProduzido() {
        
        this.getProgramacao().clear();

        return this;



    }

    public static Lote criarNovo(String nome, Long quantidade, LocalDateTime dataHoraInicio, String descricao) {
        return new Lote(nome, quantidade, dataHoraInicio, descricao);
    }

}
