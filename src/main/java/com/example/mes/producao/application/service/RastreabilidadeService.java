package com.example.mes.producao.application.service;

import java.time.LocalDateTime;
import java.util.List;
import org.springframework.stereotype.Service;
import com.example.mes.producao.api.exception.RastreabilidadeNotFoundException;

import com.example.mes.producao.application.dto.RastreabilidadeDTO;
import com.example.mes.producao.application.mapper.RastreabilidadeMapper;
import com.example.mes.producao.domain.Equipamento;
import com.example.mes.producao.domain.Lote;
import com.example.mes.producao.domain.Rastreabilidade;
import com.example.mes.producao.domain.StatusRastreabilidade;
import com.example.mes.producao.infraestructure.RastreabilidadeRepository;

import jakarta.transaction.Transactional;

@Service
public class RastreabilidadeService {

    private final RastreabilidadeRepository rastreabilidadeRepository;
    private final RastreabilidadeMapper rastreabilidadeMapper;

    public RastreabilidadeService(RastreabilidadeRepository rastreabilidadeRepository, RastreabilidadeMapper rastreabilidadeMapper) {
        this.rastreabilidadeRepository = rastreabilidadeRepository;
        this.rastreabilidadeMapper = rastreabilidadeMapper;
    }


    @Transactional
    public void registrarEventoDeEntradaRastreabilidade(Lote lote , Equipamento equipamento,StatusRastreabilidade status,String evento) {


        Rastreabilidade rastreabilidade = new Rastreabilidade(lote, equipamento, LocalDateTime.now(), status, evento);
        rastreabilidadeRepository.save(rastreabilidade);

        
    }

    @Transactional
    public void registrarEventoDeAbastecimento(Lote lote, Equipamento equipamento,StatusRastreabilidade status,String evento) {
        Rastreabilidade rastreabilidade = rastreabilidadeRepository.findByLoteAndEquipamentoAndDataHoraSaidaIsNull(lote, equipamento)
                .orElseThrow(() -> new RastreabilidadeNotFoundException("Rastreabilidade não encontrada para lote e equipamento"));

        rastreabilidade.setDataHoraSaida(LocalDateTime.now());
        
        Rastreabilidade novaRastreabilidade = new Rastreabilidade(lote, equipamento, LocalDateTime.now(), status, evento);

        novaRastreabilidade.setStatus(status);

        rastreabilidadeRepository.save(rastreabilidade);
        rastreabilidadeRepository.save(novaRastreabilidade);
    

}

    @Transactional
    public void registrarEventoRastreabilidade(Lote lote, Equipamento equipamento, StatusRastreabilidade status,String evento) {
        Rastreabilidade rastreabilidade = rastreabilidadeRepository.findByLoteAndEquipamentoAndStatus(lote, equipamento,status)
                .orElseThrow(() -> new RastreabilidadeNotFoundException("Rastreabilidade não encontrada para lote e equipamento"));

        rastreabilidade.setDataHoraSaida(LocalDateTime.now());

       

        Rastreabilidade novaRastreabilidade = new Rastreabilidade(lote, equipamento, LocalDateTime.now(), status,evento);

        novaRastreabilidade.setStatus(status);

        rastreabilidadeRepository.save(rastreabilidade);
        rastreabilidadeRepository.save(novaRastreabilidade);
}



    public RastreabilidadeDTO pegarRastreabilidadeAtual(Lote lote, Equipamento equipamento) {
        Rastreabilidade rastreabilidade = rastreabilidadeRepository.findByLoteAndEquipamentoAndDataHoraSaidaIsNull(lote, equipamento)
                .orElseThrow(() -> new RastreabilidadeNotFoundException("Rastreabilidade não encontrada para lote e equipamento"));

        return rastreabilidadeMapper.toDTO(rastreabilidade);
    }

    public RastreabilidadeDTO pegarRastreabilidadePorId(Long id) {
       Rastreabilidade rastreabilidade = rastreabilidadeRepository.findById(id).orElseThrow(() -> new RastreabilidadeNotFoundException("Rastreabilidade não encontrada para o id: " + id));

       return rastreabilidadeMapper.toDTO(rastreabilidade);
    }


    
    public List<RastreabilidadeDTO> buscarRastreabilidadeComFiltros(String lote, Long equipamentoId, String status, LocalDateTime dataInicio, LocalDateTime dataFim) {
        String loteFiltro = (lote != null && !lote.isBlank()) ? lote : null;
        StatusRastreabilidade statusEnum = null;
    
    if (status != null && !status.isBlank()) {
            statusEnum = StatusRastreabilidade.valueOf(status.toUpperCase());
        } 

        List<Rastreabilidade> rastreabilidades = rastreabilidadeRepository.buscarComFiltrosDinamicos(loteFiltro, equipamentoId, statusEnum, dataInicio, dataFim);
        if(rastreabilidades.isEmpty()) {
            throw new RastreabilidadeNotFoundException("Nenhuma rastreabilidade encontrada com os filtros aplicados");
        }
        return rastreabilidades.stream().map(r -> rastreabilidadeMapper.toDTO(r)).toList();
  
    }


    public List<RastreabilidadeDTO> pegarTodasRastreabilidades() {
        List<Rastreabilidade> rastreabilidades = rastreabilidadeRepository.findAll();

        if(rastreabilidades.isEmpty()) {
            throw new RastreabilidadeNotFoundException("Nenhuma rastreabilidade encontrada");
        }

        return rastreabilidades.stream().map(r -> rastreabilidadeMapper.toDTO(r)).toList();
    
    }    
             
    }





