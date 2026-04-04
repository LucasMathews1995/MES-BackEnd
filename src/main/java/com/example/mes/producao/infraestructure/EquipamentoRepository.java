package com.example.mes.producao.infraestructure;

import com.example.mes.producao.domain.Equipamento;
import com.example.mes.producao.domain.StatusEquipamento;
import com.example.mes.producao.domain.StatusProgramacao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EquipamentoRepository extends JpaRepository<Equipamento, Long> {


    boolean existsByNome(String nome);


}
