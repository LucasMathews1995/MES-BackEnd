package com.example.mes.producao.equipamento.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;
import com.example.mes.producao.equipamento.model.Equipamento;

@Repository
public interface EquipamentoRepository extends JpaRepository<Equipamento, Long> {

    boolean existsByNomeIgnoreCase(String nome);

    @Query("SELECT CASE WHEN COUNT(e) > 0 THEN true ELSE false END FROM Equipamento e WHERE LOWER(REPLACE(e.nome, ' ', '')) = LOWER(REPLACE(:nomeBusca, ' ', ''))")
    boolean checarSeNomeAchatadoExiste(@Param("nomeBusca") String nomeBusca);


    List<Long> findAllByIdIn(List<Long> ids);

    @Query("SELECT e.nome FROM Equipamento e WHERE e.nome IN :nomes")
    List<String> findNomesExistentes(@Param("nomes") List<String> nomes);

}
