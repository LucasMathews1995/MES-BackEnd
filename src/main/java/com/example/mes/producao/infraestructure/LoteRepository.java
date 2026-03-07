package com.example.mes.producao.infraestructure;

import com.example.mes.producao.domain.Lote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LoteRepository  extends JpaRepository<Lote, Long> {
}
