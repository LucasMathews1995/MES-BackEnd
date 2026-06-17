package com.example.mes.producao.lote.domain;

import com.example.mes.producao.lote.domain.exceptions.LoteAbastecidoException;
import com.example.mes.producao.lote.domain.exceptions.NotFoundLoteException;
import com.example.mes.producao.ordemproducao.domain.OrdemProducao;
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
    private Integer quantidadeDisponivel;

    private LocalDateTime dataCriacao;
    private String descricao;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private StatusLote status;

    private Lote(String nome, Integer quantidadeDisponivel, LocalDateTime dataCriacao, String descricao) {
        this.nome = nome;
        this.quantidadeDisponivel = quantidadeDisponivel;
        this.status = StatusLote.DESABASTECIDO;
        this.dataCriacao = dataCriacao;
        this.descricao = descricao;
    }

    private Lote() {
    }

    public void reservarLote(Integer quantidadeConsumida) {
        if (this.status == StatusLote.ABASTECIDO || this.status == StatusLote.CONSUMIDO) {
            throw new LoteAbastecidoException("O lote já está abastecido ou consumido.");
        }
        // if (this.ordemProducao == null) {
        // throw new OPNotValidException("O lote deve estar associado a uma ordem de
        // produção para ser abastecido.");
        // }
        this.quantidadeDisponivel -= quantidadeConsumida;

        this.status = StatusLote.RESERVADO;
    }

    public void consumirQuantidade(Integer quantidade) {
        if (quantidade <= 0) {
            throw new LoteAbastecidoException("A quantidade a ser consumida deve ser maior que zero.");
        }
        if (quantidade > this.quantidadeDisponivel) {
            throw new LoteAbastecidoException(
                    "A quantidade a ser consumida é maior do que a quantidade disponível no lote.");
        }
        this.quantidadeDisponivel -= quantidade;
    }

    public void desabastecerLote() {
        if (this.status != StatusLote.ABASTECIDO) {
            throw new LoteAbastecidoException("O lote " + this.nome + " não está abastecido.");
        }
        if (programacao == null) {
            throw new NotFoundLoteException("Programação associada ao lote não encontrada.");
        }
        if (programacao.getFirst().getStatus() != StatusProgramacao.EM_EXECUCAO) {
            throw new LoteAbastecidoException(
                    "A programação associada ao lote não está em produção, portanto o lote não pode ser desabastecido.");
        }

        this.status = StatusLote.DESABASTECIDO;
    }

    public void rejeitarLote(Integer quantidade) {
        if (this.status != StatusLote.ABASTECIDO || this.status != StatusLote.CONSUMIDO) {
            throw new LoteAbastecidoException("O lote " + this.nome + " não está abastecido ou consumido.");
        }

        this.quantidadeDisponivel += quantidade;
        this.status = StatusLote.QUALIDADE;

    }

    public void abastecerLote() {
        if (this.status != StatusLote.RESERVADO) {
            throw new LoteAbastecidoException("O lote " + this.nome + " não está reservado.");
        }
        // if (this.ordemProducao == null) {
        // throw new OPNotValidException("O lote deve estar associado a uma ordem de
        // produção para ser abastecido.");
        // }
       

        this.status = StatusLote.ABASTECIDO;

    }

    public void consumirLote(Integer quantidade) {
      

        this.status = StatusLote.CONSUMIDO;
    }


    public Lote gerarFilhoParaProgramacao( Integer quantidadeDesejada) {

        if (this.getQuantidadeDisponivel() < quantidadeDesejada) {
            throw new LoteAbastecidoException(
                    "Saldo insuficiente no lote " + this.getNome() +
                            ". Disponível: " + this.getQuantidadeDisponivel());
        }

        int novaQuantidadePai = this.getQuantidadeDisponivel() - quantidadeDesejada;
        lotePai.setQuantidadeDisponivel(novaQuantidadePai);

        char prefixo = nome.charAt(0);
        int numeros = Integer.parseInt(nome.substring(1));
        int resultado = numeros + 1;

        Lote loteFilho = new Lote();
        loteFilho.setNome(prefixo + String.valueOf(resultado));
        loteFilho.setQuantidadeDisponivel(quantidadeDesejada);
        loteFilho.setDataCriacao(LocalDateTime.now());
        loteFilho.setDescricao("Produzido a partir do lote pai: " + lotePai.getNome());
        loteFilho.setLotePai(this);
        loteFilho.setStatus(StatusLote.DESABASTECIDO);

        return loteFilho;
    }

   public static Lote criarNovo(String nome, Integer quantidade, LocalDateTime dataHoraInicio, String descricao) {
        return new Lote(nome, quantidade, dataHoraInicio, descricao);
    }

   

    

}
