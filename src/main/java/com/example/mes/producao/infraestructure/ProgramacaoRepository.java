package com.example.mes.producao.infraestructure;

import com.example.mes.producao.domain.Programacao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProgramacaoRepository  extends JpaRepository<Programacao,Long> {

    @Query(value = "SELECT * FROM tb_programacao WHERE equipamento_id = :equipamento ORDER BY sequencia_fila ASC", nativeQuery = true)
    Optional<List<Programacao>> findAllProgramacaoByEquipamentoId(@Param("equipamento") Long equipamentoId);

    @Query(value ="SELECT MAX(sequencia_fila)  FROM tb_programacao",nativeQuery= true )
    Optional<Integer> findMaxSequenciaFila();


}
