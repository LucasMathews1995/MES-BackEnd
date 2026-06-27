package com.example.mes.producao.programacao.infraestructure.persistence;



import org.springframework.data.repository.query.Param;
import com.example.mes.producao.programacao.domain.Programacao;
import com.example.mes.producao.programacao.domain.StatusProgramacao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;


public interface ProgramacaoRepository extends JpaRepository<Programacao, Long> {
@Query("SELECT COALESCE(MAX(p.fila), 0) FROM Programacao p WHERE p.equipamento.id = :equipamentoId AND p.status = :status")
Integer findMaxOrdemByEquipamentoAndStatus(@Param("equipamentoId") Long equipamentoId, @Param("status") StatusProgramacao status);

}
