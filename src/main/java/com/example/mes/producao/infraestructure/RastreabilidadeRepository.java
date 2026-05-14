package com.example.mes.producao.infraestructure;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;
import com.example.mes.producao.domain.Equipamento;
import com.example.mes.producao.domain.Lote;
import com.example.mes.producao.domain.Rastreabilidade;
import com.example.mes.producao.domain.StatusRastreabilidade;

@Repository
public interface RastreabilidadeRepository  extends JpaRepository<Rastreabilidade, Long> {

    Optional<Rastreabilidade> findByLoteAndEquipamentoAndDataHoraSaidaIsNull(Lote lote, Equipamento equipamento);

    Optional<Rastreabilidade> findByLoteAndEquipamentoAndStatus(Lote lote, Equipamento equipamento,
            StatusRastreabilidade status);

@Query("SELECT r FROM Rastreabilidade r WHERE " +
           "(:lote IS NULL OR r.lote.nome = :lote) AND " +
           "(:equipamentoId IS NULL OR r.equipamento.id = :equipamentoId) AND " +
           "(:status IS NULL OR r.status = :status) AND " +
           "(CAST(:inicio AS timestamp) IS NULL OR r.dataHoraEntrada >= :inicio) AND " +
           "(CAST(:fim AS timestamp) IS NULL OR r.dataHoraEntrada <= :fim)")
    List<Rastreabilidade> buscarComFiltrosDinamicos(
            @Param("lote") String lote,
            @Param("equipamentoId") Long equipamentoId,
            @Param("status") StatusRastreabilidade status,
            @Param("inicio") LocalDateTime inicio,
            @Param("fim") LocalDateTime fim
    );

}
