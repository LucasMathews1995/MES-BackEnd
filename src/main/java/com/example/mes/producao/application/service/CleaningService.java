package com.example.mes.producao.application.service;

import com.example.mes.producao.domain.StatusOP;
import com.example.mes.producao.infraestructure.OrdemProducaoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class CleaningService {


    private final OrdemProducaoRepository ordemProducaoRepository;


    @Scheduled(cron = "0 15 10 * * *")
    @Transactional
    public void finalizarOrdensAntigas(){
        LocalDate antigas = LocalDate.now().minusMonths(1);
        int totalDeletado = 0;
        int deletadosNoLote;

        if(antigas.isBefore(LocalDate.now())) {
        do {

            deletadosNoLote = ordemProducaoRepository.deleteTop500Custom();
            totalDeletado += deletadosNoLote;
        } while (deletadosNoLote > 0);}

        System.out.println("Faxina concluída. Total: " + totalDeletado);
    }
}
