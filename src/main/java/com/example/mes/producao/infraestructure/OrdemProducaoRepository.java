package com.example.mes.producao.infraestructure;

import com.example.mes.producao.domain.OrdemProducao;
import com.example.mes.producao.domain.StatusOP;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;



@Repository
public interface OrdemProducaoRepository extends JpaRepository<OrdemProducao,Long> {

    boolean existsByNumeroOP(String numeroOP);

    @Modifying
    @Transactional
    @Query(value = "DELETE  FROM tb_ordem_producao where status = 'FINALIZADA' LIMIT 500",
            nativeQuery = true)
    int deleteTop500Custom();


}
