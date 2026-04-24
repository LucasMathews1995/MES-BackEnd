package com.example.mes.producao.infraestructure;

import com.example.mes.producao.application.dto.ProgramacaoOrdemProducaoDTO;
import com.example.mes.producao.domain.Programacao;
import com.example.mes.producao.domain.StatusProgramacao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;


@Repository
public interface ProgramacaoRepository extends JpaRepository<Programacao, Long> {




    @Query("SELECT MAX(p.fila) FROM Programacao p WHERE p.equipamento.id = :equipamentoId")
    Integer findMaxFilaByEquipamentoId(@Param("equipamentoId") Long equipamentoId);

    boolean existsByLoteId(Long loteId);


    List<Programacao> findAllByEquipamentoIdAndStatus(Long equipamentoId, StatusProgramacao status);


    List<Programacao> findByEquipamentoIdAndStatusNotIn(Long equipamentoId,List<StatusProgramacao> status);

    List<Programacao> findByEquipamentoIdAndStatus(Long equipamento_id, StatusProgramacao status);


    @Query("SELECT new com.example.mes.producao.application.dto.ProgramacaoOrdemProducaoDTO(" +
            "p.id, p.dataHoraProgramada, p.fila, p.quantidadeConsumida, l.nome, p.status, op.numeroOP) " +
            "FROM Programacao p " +
            "JOIN p.lote l " +
            "JOIN l.ordemProducao op " +
            "WHERE p.equipamento.id = :equipamentoId")
    List<ProgramacaoOrdemProducaoDTO> findProgramacaoByEquipamento(@Param("equipamentoId") Long equipamentoId);

    @Query("SELECT new com.example.mes.producao.application.dto.ProgramacaoOrdemProducaoDTO(" +
            "p.id, p.dataHoraProgramada, p.fila, p.quantidadeConsumida, l.nome, p.status, op.numeroOP) " +
            "FROM Programacao p " +
            "JOIN p.lote l " +
            "JOIN l.ordemProducao op " +
            "WHERE p.status = 'CRIADA'")
    List<ProgramacaoOrdemProducaoDTO> findProgramacoesCriadas(@Param("equipamentoId") Long equipamentoId);



}
