package com.example.mes.rastreabilidade.infraestructure.persistence;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.mes.rastreabilidade.domain.Rastreabilidade;


@Repository
public interface RastreabilidadeRepository  extends JpaRepository<Rastreabilidade, Long> {




}
