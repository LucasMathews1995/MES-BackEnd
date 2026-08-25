package com.example.mes.producao.lote.infraestructure.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.mes.producao.lote.domain.Lote;
import com.example.mes.producao.programacao.domain.Programacao;

import java.util.List;
import java.util.Optional;

@Repository
public interface LoteRepository  extends JpaRepository<Lote, Long> {
    boolean existsByNome(String nomeCandidato);

    @Query(value = "SELECT * FROM tb_programacao WHERE lote_id = :loteId ORDER BY id DESC LIMIT 1", nativeQuery = true)
    Optional<Programacao> buscarUltimaProgramacaoPorLote(@Param("loteId") Long loteId);
    @Query(value = "SELECT * FROM tb_lote WHERE ordem_producao_id = :ordemProducaoId ORDER BY id ",nativeQuery = true)
    Optional<List<Lote>> buscarPorOrdemProducao(@Param("ordemProducao") Long ordemProducaoId);   
    @Query(value = "SELECT * FROM tb_lote WHERE ordem_producao_id IS NULL ORDER BY id LIMIT 30",nativeQuery = true)
    Optional<List<Lote>> buscarSemOrdemProducao ();
    

    
}
