package com.example.mes.producao.ordemproducao.infraestructure.persistence;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import com.example.mes.producao.ordemproducao.domain.OrdemProducao;



@Repository
public interface OrdemProducaoRepository extends JpaRepository<OrdemProducao,Long> {

    boolean existsByNumeroOP(String numeroOP);

    @Modifying
    @Transactional
   @Query(value = "DELETE FROM tb_ordem_producao WHERE id IN (SELECT id FROM tb_ordem_producao WHERE status = 'FINALIZADA' LIMIT 500)", nativeQuery = true)
    int deleteTop500Custom();


    Slice<OrdemProducao> findAllByOrderByIdDesc(Pageable pageable);


}
