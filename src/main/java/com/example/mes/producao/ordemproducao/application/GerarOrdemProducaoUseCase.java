package com.example.mes.producao.ordemproducao.application;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import com.example.mes.producao.equipamento.exceptions.NotFoundEquipamentoException;
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

        OrdemProducao op = OrdemProducao.criarNormal(dto.equipamentoId(), darNomeOP(), dto.capacidadeMaxima());

        if (!equipamentoRepository.existsById(dto.equipamentoId())) {
            throw new NotFoundEquipamentoException("Equipamento não encontrado");
        }

        repository.save(op);
        return OrdemProducaoOuputDTO.fromEntity(op);
    }

    @Transactional
    public OrdemProducaoOuputDTO salvarRetrabalho(OrdemProducaoInputDTO dto) {
        OrdemProducao op = OrdemProducao.criarRetrabalho(dto.equipamentoId(), darNomeOP(), dto.capacidadeMaxima());

        repository.save(op);
        return OrdemProducaoOuputDTO.fromEntity(op);
    }

    @Transactional
    public List<OrdemProducaoOuputDTO> salvarRetrabalhoLista(List<OrdemProducaoInputDTO> dto) {
        List<OrdemProducao> novaOps = dto.stream().map(it -> OrdemProducao
                .criarRetrabalho(it.equipamentoId(), darNomeOP(), it.capacidadeMaxima()))
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

        repository.saveAll(novaOps);

        return novaOps.stream().map(OrdemProducaoOuputDTO::fromEntity).collect(Collectors.toList());
    }

    @Transactional
    public List<OrdemProducaoOuputDTO> salvarListaOPs(List<OrdemProducaoInputDTO> dto) {

        List<OrdemProducao> novasOps = dto.stream()
                .map(it -> OrdemProducao.criarNormal(
                        it.equipamentoId(),
                        darNomeOP(),
                        it.capacidadeMaxima()))
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
