package com.example.mes.producao.programacao.infraestructure.persistence;



import org.springframework.data.repository.query.Param;

import com.example.mes.producao.lote.domain.Lote;
import com.example.mes.producao.programacao.domain.Programacao;
import com.example.mes.producao.programacao.domain.StatusProgramacao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;


public interface ProgramacaoRepository extends JpaRepository<Programacao, Long> {
@Query("SELECT COALESCE(MAX(p.fila), 0) FROM Programacao p WHERE p.equipamento.id = :equipamentoId AND p.status = :status")
Integer findMaxOrdemByEquipamentoAndStatus(@Param("equipamentoId") Long equipamentoId, @Param("status") StatusProgramacao status);
@Query("""
    SELECT CASE WHEN (
        COUNT(p) > 0 AND 
        SUM(CASE WHEN p.status <> :statusConcluida THEN 1 ELSE 0 END) = 0
    ) THEN true ELSE false END
    FROM Programacao p
    WHERE p.loteConsumido.id = :loteId
""")
boolean todasProgramacoesEstaoConcluidas(
    @Param("loteId") Long loteId, 
    @Param("statusConcluida") StatusProgramacao statusConcluida
);
}
