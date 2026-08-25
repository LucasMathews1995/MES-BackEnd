package com.example.mes.producao.equipamento.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import com.example.mes.producao.equipamento.exceptions.EquipamentoNotValidException;
import com.example.mes.producao.programacao.domain.Programacao;
import com.example.mes.producao.programacao.domain.StatusProgramacao;
import com.example.mes.rastreabilidade.domain.Rastreabilidade;

@Entity
@Getter
@Setter
@ToString()
@EqualsAndHashCode()
@Table(name = "tb_equipamento")
public class Equipamento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false, length = 50)
    private String nome;

    private String sigla;

    @OneToMany(mappedBy = "equipamento", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Programacao> programacao = new HashSet<>();

    @OneToMany(mappedBy = "equipamento", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Rastreabilidade> rastreabilidade = new HashSet<>();


    @Column(nullable = false, name = "ativo")
    private boolean isAtivo;

    private String descricao;

    private LocalDateTime dataAtivacao;

    private LocalDateTime dataParado;

    @Enumerated(EnumType.STRING)
    private StatusEquipamento statusEquipamento;

    @Column(precision = 10, nullable = false)
    private Long capacidade;

    public Equipamento(String nome, String sigla, String descricao, LocalDateTime dataAtivacao, Long capacidade) {

        this.nome = nome;
        this.sigla = sigla;
        this.descricao = descricao;
        this.dataAtivacao = dataAtivacao;
        this.dataParado = null;
        isAtivo = true;
        this.statusEquipamento = StatusEquipamento.OPERANDO;
        this.capacidade = capacidade;
    }

    public Equipamento() {

    }

    public void removerProgramacao(Programacao programacao) {

        this.programacao.remove(programacao);
        programacao.setEquipamento(null);

    }

    public void adicionarProgramacao(Programacao programacao) {
        this.programacao.add(programacao);
        programacao.setEquipamento(this);
    }

    public void adicionarRastreabilidade(Rastreabilidade rastreabilidade) {
        this.rastreabilidade.add(rastreabilidade);
        rastreabilidade.setEquipamento(this);
    }

    public void removerRastreabilidade(Rastreabilidade rastreabilidade) {

        this.rastreabilidade.remove(rastreabilidade);
        rastreabilidade.setEquipamento(null);

    }

    public void desativarEquipamento() {

        this.statusEquipamento = StatusEquipamento.PARADO;
        this.dataParado = LocalDateTime.now();
        this.isAtivo = false;

    }

    public void ativarEquipamento() {
        setStatusEquipamento(StatusEquipamento.OPERANDO);
        setDataParado(null);
        setDataAtivacao(LocalDateTime.now());
        setAtivo(true);
    }

    public void diminuirCapacidade(Long quantidadeConsumida) {
        if (this.capacidade < quantidadeConsumida) {
            throw new EquipamentoNotValidException(
                    "A quantidade consumida é maior do que a capacidade do equipamento.");
        }
        this.capacidade = this.capacidade - quantidadeConsumida;
    }

    public void acrescerCapacidade(Long quantidadeConsumida) {

        this.capacidade = this.capacidade + quantidadeConsumida;
    }

    public void alterarCapacidade(Long capacidade) {
        boolean existe = programacao.stream().anyMatch(it -> it.getStatus().equals(StatusProgramacao.PROGRAMADA)
                && it.getStatus().equals(StatusProgramacao.EM_EXECUCAO));
        if (existe) {
            throw new EquipamentoNotValidException(
                    "Não pode alterar  a capacidade pois ainda tem programações dentro do equipamento: " + this.nome);
        }

        if (capacidade == null) {
            return;
        }

        this.capacidade = capacidade;

    }

}
