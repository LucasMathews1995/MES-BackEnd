package com.example.mes.producao.infraestructure;

import com.example.mes.producao.domain.OrdemProducao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrdemProducaoRepository extends JpaRepository<OrdemProducao,Long> {

    boolean existsByNumeroOP(String numeroOP);

}
