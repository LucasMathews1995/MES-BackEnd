package com.example.mes.producao.infraestructure;

import com.example.mes.producao.domain.Programacao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProgramacaoRepository  extends JpaRepository<Programacao,Long> {
}
