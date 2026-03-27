package com.example.mes.producao.domain;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;


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

    @Column(length = 100,  nullable = false , unique = true,name = "numero_op")
    @Getter
    @Setter
    private String numeroOP;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Getter
    @Setter
    private StatusOP status;

    @Column(nullable = false , name  = "data_criacao")
    @Getter
    private LocalDateTime dataCriacao;

    @Column(name = "data_encerramento")
    @Getter
    private LocalDateTime dataEncerramento;




    @OneToMany(mappedBy = "ordemProducao")
    @Getter
    private Set<Lote> lotes = new HashSet<>();



    public OrdemProducao() {
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






}
