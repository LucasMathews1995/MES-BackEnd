package com.example.mes.producao.programacao.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;
import com.example.mes.producao.equipamento.exceptions.EquipamentoNotValidException;
import com.example.mes.producao.equipamento.model.Equipamento;
import com.example.mes.producao.lote.domain.Lote;
import com.example.mes.producao.ordemproducao.domain.OrdemProducao;
import com.example.mes.producao.ordemproducao.exceptions.OPNotValidException;


@Entity
@Table(name = "tb_programacao", uniqueConstraints = {
        @UniqueConstraint(name = "uk_equipamento_fila", columnNames = { "equipamento_id", "fila" })
})
@Getter
@Setter
public class Programacao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lote_consumido_id")
    private Lote loteConsumido;

    @OneToOne(cascade = CascadeType.ALL)
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
    private Integer quantidadeConsumida;

    private Programacao(Lote loteConsumido,Lote loteProduzido, Equipamento equipamento, StatusProgramacao statusProgramacao,
            Integer quantidadeConsumida) {
        this.equipamento = equipamento;
        this.loteConsumido = loteConsumido;
        this.loteProduzido = loteProduzido;
        this.status = statusProgramacao;
        this.dataHoraProgramada = LocalDateTime.now();
        this.quantidadeConsumida = quantidadeConsumida;

    }

    public Programacao() {

    }

    public static Programacao criarPrograma(OrdemProducao producao,Equipamento equipamento, Lote loteConsumido,Lote loteProduzido, Integer quantidadeConsumida) {
         Programacao programacao = null;

        if (!equipamento.isAtivo()) {
            throw new EquipamentoNotValidException("O equipamento não está ativo");
        }
        if(!producao.getEquipamentoId().equals(equipamento.getId())){
            throw new OPNotValidException("A ordem de produção está destinada a outro equipamento :" + producao.getEquipamentoId());
        }
        

         programacao = new Programacao(loteConsumido,loteProduzido, equipamento, StatusProgramacao.CRIADA, quantidadeConsumida);
        return programacao;

    }

   

    public void setFila(Integer ultimaFila) {

        if (ultimaFila != null) {
            this.fila = ultimaFila + 1;
        } else {
            this.fila = null;
        }

    }

    public void programarLote(){
        
       
        this.setStatus(StatusProgramacao.PROGRAMADA);
    }


   

}
