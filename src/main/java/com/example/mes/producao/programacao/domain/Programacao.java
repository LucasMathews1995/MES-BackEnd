package com.example.mes.producao.programacao.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;
import com.example.mes.producao.equipamento.exceptions.EquipamentoNotValidException;
import com.example.mes.producao.equipamento.model.Equipamento;
import com.example.mes.producao.lote.domain.Lote;
import com.example.mes.producao.ordemproducao.domain.OrdemProducao;
import com.example.mes.producao.ordemproducao.domain.StatusOP;
import com.example.mes.producao.ordemproducao.exceptions.OrdemProducaoNotValidException;
import com.example.mes.producao.programacao.domain.exceptions.ProgramacaoNotValidException;

@Entity
@Table(name = "tb_programacao", uniqueConstraints = {
        @UniqueConstraint(name = "uk_equipamento_fila_status", columnNames = { "equipamento_id", "fila", "status" })
})
@Getter
@Setter
public class Programacao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ordem_producao_id")
    private OrdemProducao ordemProducao;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lote_consumido_id")
    private Lote loteConsumido;

    @OneToOne(cascade = { CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE }, orphanRemoval = true)
    @JoinColumn(name = "lote_produzido_id")
    private Lote loteProduzido;

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

    @Column(precision = 6, nullable = false)
    private Long quantidadeConsumida;

    private Programacao(OrdemProducao ordemProducao, Lote loteConsumido, Lote loteProduzido, Equipamento equipamento,
            StatusProgramacao statusProgramacao,
            Long quantidadeConsumida) {
        this.ordemProducao = ordemProducao;
        this.equipamento = equipamento;
        this.loteConsumido = loteConsumido;
        this.loteProduzido = loteProduzido;
        this.status = statusProgramacao;
        this.dataHoraProgramada = LocalDateTime.now();
        this.quantidadeConsumida = quantidadeConsumida;

    }

    public Programacao() {

    }

    public static Programacao criarPrograma(OrdemProducao ordemProducao, Equipamento equipamentoNovo,
            Lote loteConsumido,
            Lote loteProduzido  ) {

        Programacao programacao = null;

        if (!equipamentoNovo.isAtivo()) {
            throw new EquipamentoNotValidException("O equipamento não está ativo");
        }
        if (equipamentoNovo != ordemProducao.getEquipamento()) {
            throw new OrdemProducaoNotValidException(
                    "A ordem de produção: " + ordemProducao.getId() + " e o equipamento: " + equipamentoNovo.getId()
                            + " não são compatíveis");
        }

        if (!ordemProducao.getStatus().equals(StatusOP.PROCESSANDO)) {
            throw new OrdemProducaoNotValidException("A Ordem de Producao não foi processada");
        }

        programacao = new Programacao(ordemProducao, loteConsumido, loteProduzido, equipamentoNovo,
                StatusProgramacao.CRIADA,
                loteProduzido.getQuantidadeDisponivel());

        return programacao;

    }

    public void setFila(Integer ultimaFila) {

        if (ultimaFila != null) {
            this.fila = ultimaFila + 1;
        } else {
            this.fila = null;
        }

    }

    public void programarLote(Integer ultimaFila) {

        this.setStatus(StatusProgramacao.PROGRAMADA);
        this.setFila(ultimaFila);
    }

    public void cancelarProgramacao() {
        this.setStatus(StatusProgramacao.CANCELADA);
        this.setFila(null);

    }

    public void colocarEmQualidade() {
        if (this.status != StatusProgramacao.PROGRAMADA && this.status != StatusProgramacao.CRIADA && this.status != StatusProgramacao.DESABASTECIDO) {
            throw new ProgramacaoNotValidException(
                    "A programação deve estar em status PROGRAMADA ou CRIADA para ser colocada em QUALIDADE.");

        }
        this.setStatus(StatusProgramacao.QUALIDADE);

        this.setFila(null);
        this.equipamento.acrescerCapacidade(quantidadeConsumida);
        this.setLoteProduzido(null);
       

    }

    public void desabastecerProgramacao() {
        if (this.status != StatusProgramacao.EM_EXECUCAO ){
                
            throw new ProgramacaoNotValidException(
                    "A programação deve estar em status EM_EXECUCAO para ser desabastecida.");
        }

        this.setStatus(StatusProgramacao.DESABASTECIDO);
        this.setFila(null);
        this.equipamento.acrescerCapacidade(quantidadeConsumida);

    }

    public void retirarDaQualidade() {

        this.setStatus(StatusProgramacao.CRIADA);
        

    }

}
