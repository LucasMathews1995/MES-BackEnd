package com.example.mes.producao.infraestructure;

import com.example.mes.producao.domain.Lote;
import com.example.mes.producao.domain.Programacao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LoteRepository  extends JpaRepository<Lote, Long> {
    boolean existsByNome(String nomeCandidato);

    @Query(value = "SELECT * FROM tb_programacao WHERE lote_id = :loteId ORDER BY id DESC LIMIT 1", nativeQuery = true)
    Optional<Programacao> buscarUltimaProgramacaoPorLote(@Param("loteId") Long loteId);
}
