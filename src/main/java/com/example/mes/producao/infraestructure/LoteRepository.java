package com.example.mes.producao.infraestructure;

import com.example.mes.producao.domain.Lote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LoteRepository  extends JpaRepository<Lote, Long> {
    boolean existsByLoteNome(String nomeCandidato);

    Optional<Lote> findByLoteNome(String loteNome);
}
