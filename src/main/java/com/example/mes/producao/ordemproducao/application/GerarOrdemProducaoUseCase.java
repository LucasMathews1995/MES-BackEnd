package com.example.mes.producao.ordemproducao.application;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import java.util.Map;
import org.springframework.stereotype.Service;
import com.example.mes.producao.equipamento.exceptions.NotFoundEquipamentoException;
import com.example.mes.producao.equipamento.model.Equipamento;
import com.example.mes.producao.equipamento.repositories.EquipamentoRepository;
import com.example.mes.producao.ordemproducao.domain.OrdemProducao;
import com.example.mes.producao.ordemproducao.dto.OrdemProducaoInputDTO;
import com.example.mes.producao.ordemproducao.dto.OrdemProducaoOuputDTO;
import com.example.mes.producao.ordemproducao.infraestructure.persistence.OrdemProducaoRepository;
import jakarta.transaction.Transactional;

@Service
public class GerarOrdemProducaoUseCase {

    private final OrdemProducaoRepository repository;
    private final EquipamentoRepository equipamentoRepository;

    public GerarOrdemProducaoUseCase(OrdemProducaoRepository repository, EquipamentoRepository equipamentoRepository) {
        this.repository = repository;

        this.equipamentoRepository = equipamentoRepository;
    }

    @Transactional
    public OrdemProducaoOuputDTO salvarOrdemProducao(OrdemProducaoInputDTO dto) {

        Equipamento equipamento = equipamentoRepository.findById(dto.equipamentoId())
                .orElseThrow(() -> new NotFoundEquipamentoException("Equipamento não encontrado"));

        OrdemProducao op = OrdemProducao.criarNormal(equipamento, darNomeOP(), dto.capacidadeMaxima());

        repository.save(op);
        return OrdemProducaoOuputDTO.fromEntity(op);
    }

    @Transactional
    public OrdemProducaoOuputDTO salvarRetrabalho(OrdemProducaoInputDTO dto) {
        Equipamento equipamento = equipamentoRepository.findById(dto.equipamentoId())
                .orElseThrow(() -> new NotFoundEquipamentoException("Equipamento não encontrado"));

        OrdemProducao op = OrdemProducao.criarRetrabalho(equipamento, darNomeOP(), dto.capacidadeMaxima());

        repository.save(op);
        return OrdemProducaoOuputDTO.fromEntity(op);
    }

@Transactional
    public List<OrdemProducaoOuputDTO> salvarRetrabalhoLista(List<OrdemProducaoInputDTO> dto) {
        
        
        List<Long> idsEquipamentos = dto.stream()
                .map(OrdemProducaoInputDTO::equipamentoId)
                .distinct()
                .toList();

       
        Map<Long, Equipamento> equipamentosMap = equipamentoRepository.findAllById(idsEquipamentos)
                .stream()
                .collect(Collectors.toMap(Equipamento::getId, eq -> eq));

       
        if (equipamentosMap.size() != idsEquipamentos.size()) {
            throw new NotFoundEquipamentoException("Um ou mais equipamentos informados não foram encontrados.");
        }

        List<OrdemProducao> novaOps = dto.stream().map(it -> {
            Equipamento equipamento = equipamentosMap.get(it.equipamentoId());
           return OrdemProducao.criarRetrabalho(equipamento, darNomeOP(), it.capacidadeMaxima());
        }).toList();


        repository.saveAll(novaOps);

        return novaOps.stream()
                .map(OrdemProducaoOuputDTO::fromEntity)
                .toList(); 
    }

    
    @Transactional
    public List<OrdemProducaoOuputDTO> salvarListaOPs(List<OrdemProducaoInputDTO> dto) {

        List<OrdemProducao> novasOps = dto.stream()
                .map(it -> {
                    Equipamento equipamento = equipamentoRepository.findById(it.equipamentoId())
                            .orElseThrow(() -> new NotFoundEquipamentoException("Equipamento não encontrado"));
                    return OrdemProducao.criarNormal(equipamento, darNomeOP(), it.capacidadeMaxima());
                })
                .toList();

        List<Long> idsEquipamentos = dto.stream()
                .map(OrdemProducaoInputDTO::equipamentoId)
                .distinct()
                .toList();

        List<Long> idsExistentes = equipamentoRepository.findAllByIdIn(idsEquipamentos);
        boolean contemInexistentes = idsExistentes.size() != idsEquipamentos.size();

        if (contemInexistentes) {
            throw new NotFoundEquipamentoException("Um ou mais equipamentos informados não foram encontrados.");
        }

        repository.saveAll(novasOps);

        return novasOps.stream().map(OrdemProducaoOuputDTO::fromEntity).collect(Collectors.toList());
    }

    private String darNomeOP() {
        String nome;

        do {
            String prefixo = "OP";
            int numero = new Random().nextInt(999_999);
            nome = String.format("%s%d", prefixo, numero);

        } while (repository.existsByNumeroOP(nome));

        return nome;
    }
}
