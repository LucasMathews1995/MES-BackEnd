package com.example.mes.producao.lote.application;

import java.util.Random;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import com.example.mes.producao.lote.domain.Lote;
import com.example.mes.producao.lote.domain.event.LoteCreatedEvent;
import com.example.mes.producao.lote.infraestructure.dto.CriarLoteInputDTO;
import com.example.mes.producao.lote.infraestructure.dto.LoteOutputDTO;
import com.example.mes.producao.lote.infraestructure.persistence.LoteRepository;


import jakarta.transaction.Transactional;

@Service
public class CriarLoteUseCase {


   private final LoteRepository loteRepository;
   private final ApplicationEventPublisher applicationEventPublisher;

    public CriarLoteUseCase(LoteRepository loteRepository, ApplicationEventPublisher applicationEventPublisher) {
        this.loteRepository = loteRepository;
        this.applicationEventPublisher = applicationEventPublisher;
    }

    @Transactional
    public LoteOutputDTO executar(CriarLoteInputDTO input) {
       Lote lote =  Lote.criarNovo(gerarNomeLoteUnico(), input.quantidade(), input.dataHoraInicio(), input.descricao());
       

        loteRepository.save(lote);

      applicationEventPublisher.publishEvent(new LoteCreatedEvent(lote));
        
        return LoteOutputDTO.fromEntity(lote);
    }


    
private String gerarNomeLoteUnico() {
        Random random = new Random();
      
        int numeroSorteado = random.nextInt(999_999); 
  
        String numeroFormatado = String.format("%06d", numeroSorteado); 

        char prefixo = 'A';
        String tentativaNome = prefixo + numeroFormatado;

       
        while (loteRepository.existsByNome(tentativaNome)) {
            prefixo++; 
            
            if (prefixo > 'Z') {
                throw new IllegalStateException("Limite máximo de prefixos (A-Z) atingido para a numeração: " + numeroFormatado);
            }
            
           tentativaNome = prefixo + numeroFormatado;
        }

        return tentativaNome +"0100";
    }
 
    
}
