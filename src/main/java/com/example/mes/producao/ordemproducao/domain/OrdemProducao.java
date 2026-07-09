package com.example.mes.producao.ordemproducao.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import com.example.mes.producao.equipamento.model.Equipamento;
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

    
    private Long ordemVendaId;
    
    @Column(name = "equipamentoId", nullable = false)
    @Getter
    private Long equipamentoId;

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

    @Column(name = "capacidade_ocupada", nullable = false)
    @Getter
    private Long capacidadeOcupada;

    @Column(name = "capacidade_maxima", nullable = false)
    @Getter
    private long capacidadeMaxima;



    private OrdemProducao(Long equipamentoId,String numeroOP, StatusOP status, LocalDateTime dataCriacao,Long capacidadeMaxima) {
        this.numeroOP = numeroOP;
        this.status = status;
        this.dataCriacao = dataCriacao;
        this.equipamentoId = equipamentoId;
        this.capacidadeMaxima = capacidadeMaxima;
        this.capacidadeOcupada = 0L;
        
    }
    public OrdemProducao(){}
    

    public static OrdemProducao criarNormal(Long equipamentoId ,String numeroOP, Long capacidadeMaxima) {
        return new OrdemProducao(equipamentoId,numeroOP, StatusOP.INICIADA, LocalDateTime.now(), capacidadeMaxima);
    }

    public static OrdemProducao criarRetrabalho(Long equipamentoId ,String numeroOP, Long capacidadeMaxima) {
        return new OrdemProducao(equipamentoId,numeroOP, StatusOP.RETRABALHO, LocalDateTime.now(), capacidadeMaxima);
    }

    public void adicionarLote(Lote lote) {

        if (lote.getOrdemProducao() != null) {
            throw new OPNotValidException("O lote " + lote.getNome() + " já não está vinculado a uma ordem de produção");

        }
        
        if(this.capacidadeOcupada + lote.getQuantidadeDisponivel() > this.capacidadeMaxima){
            throw new OPNotValidException("A ordem de produção não possui capacidade suficiente para adicionar o lote " + lote.getNome());

        }
        this.capacidadeOcupada = getCapacidadeOcupada() + lote.getQuantidadeDisponivel();
       
        lote.setOrdemProducao(this);
        this.lotes.add(lote);


        if (this.status == StatusOP.INICIADA) {
            this.status = StatusOP.PROCESSANDO;
        }
       
    }
   


    public void finalizarLoteNaOP(Lote lote) {
        if(lote.getStatus()!=StatusLote.ABASTECIDO && lote.getStatus()!=StatusLote.CONSUMIDO){
            throw new OPNotValidException("Impossível remover lote que não está abastecido ou consumido da ordem de produção");
        }

       
        this.lotes.remove(lote);
        lote.setOrdemProducao(null);

        if (this.lotes.isEmpty() && this.status == StatusOP.PROCESSANDO) {
            this.status = StatusOP.FINALIZADA;
            this.dataEncerramento = LocalDateTime.now();
        }
      
       
    }

    public void pausarOP(Equipamento equipamento) {
     if(equipamento.isAtivo()){
        throw new OPNotValidException("Impossível pausar a ordem de produção enquanto o equipamento estiver ativo");
     }

        this.status = StatusOP.PAUSADA;
        
    }

   

    public void validarSePodeSerExcluido() {

        if (this.status != StatusOP.INICIADA && this.status != StatusOP.RETRABALHO) {

            throw new OPNotValidException(
                    "Para excluir, a ordem de produção precisa estar no status INICIADA ou RETRABALHO. Status atual: " + this.status);
        }

        boolean possuiLotesAbastecidos = this.getLotes().stream()
                .anyMatch(it -> it.getStatus() == StatusLote.ABASTECIDO);

        if (possuiLotesAbastecidos) {
            throw new OPNotValidException("Ainda há lotes abastecidos. Precisa desabastecê-los.");
        }
       
    }

    public void setStatus(StatusOP novoStatus) {
       
        this.status = novoStatus;
    }

    public void setEquipamento(Long equipamentoId) {
        if(this.equipamentoId == null){
            this.equipamentoId = equipamentoId;
        }
    }

    public void setCapacidadeOcupada(Long capacidadeOcupada) {
        this.capacidadeOcupada = capacidadeOcupada;
    }


     

    
}