package com.example.mes.producao.equipamento.service;

import java.util.List;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import com.example.mes.producao.equipamento.dto.EquipamentoOutputDTO;
import com.example.mes.producao.equipamento.dto.EquipamentoPesoUpdateDTO;
import com.example.mes.producao.equipamento.dto.EquipamentoRequestDTO;
import com.example.mes.producao.equipamento.exceptions.EquipamentoNotValidException;
import com.example.mes.producao.equipamento.exceptions.NotFoundEquipamentoException;
import com.example.mes.producao.equipamento.model.Equipamento;
import com.example.mes.producao.equipamento.repositories.EquipamentoRepository;
import com.example.mes.producao.programacao.domain.StatusProgramacao;
import com.example.mes.producao.programacao.domain.exceptions.ProgramacaoNotValidException;
import com.example.mes.rastreabilidade.domain.event.EquipamentoAtivacaoEvent;
import com.example.mes.rastreabilidade.domain.event.EquipamentoAtualizadoEvent;
import com.example.mes.rastreabilidade.domain.event.RastreabilidadeEquipamentoEvent;

import jakarta.transaction.Transactional;

@Service
public class EquipamentoService {

    private final EquipamentoRepository equipamentoRepository;
    private final ApplicationEventPublisher eventPublisher;

    public EquipamentoService(EquipamentoRepository equipamentoRepository, ApplicationEventPublisher eventPublisher) {
        this.equipamentoRepository = equipamentoRepository;
        this.eventPublisher = eventPublisher;

    }

    public List<EquipamentoOutputDTO> buscarEquipamentos() {
        List<Equipamento> equipamentos = equipamentoRepository.findAll();

        return equipamentos.stream().map(EquipamentoOutputDTO::fromEntity).toList();
    }

    public EquipamentoOutputDTO buscarEquipamentoPorId(Long id) {
        Equipamento equipamento = equipamentoRepository.findById(id)
                .orElseThrow(() -> new NotFoundEquipamentoException("Nenhum equipamento achado com esse id: " + id));

        return EquipamentoOutputDTO.fromEntity(equipamento);

    }

    @Transactional
    public List<EquipamentoOutputDTO> criarEquipamentoLista(List<EquipamentoRequestDTO> dtos) {

        List<String> nomesParaVerificar = dtos.stream()
                .map(EquipamentoRequestDTO::getNomeAchatado)
                .toList();

        List<String> nomesJaCadastrados = equipamentoRepository.findNomesExistentes(nomesParaVerificar);

        if (!nomesJaCadastrados.isEmpty()) {
            throw new EquipamentoNotValidException(
                    "Já existem equipamentos cadastrados com os nomes: " + nomesJaCadastrados);
        }

        List<Equipamento> equipamentos = dtos.stream()
                .map(it -> new Equipamento(it.nome(), it.sigla(),
                        it.descricao(), it.dataAtivacao(), it.capacidade()))
                .toList();

        equipamentoRepository.saveAll(equipamentos);

        return equipamentos.stream().map(EquipamentoOutputDTO::fromEntity).toList();

    }

    @Transactional
    public EquipamentoOutputDTO criarEquipamento(EquipamentoRequestDTO equipamento) {
        if (equipamentoRepository.checarSeNomeAchatadoExiste(equipamento.getNomeAchatado())) {
            throw new EquipamentoNotValidException("Equipamento com nome '" + equipamento.nome() + "' já existe.");
        }

        Equipamento equipamentoSalvo = new Equipamento(equipamento.nome(), equipamento.sigla(), equipamento.descricao(),
                equipamento.dataAtivacao(), equipamento.capacidade());

        equipamentoRepository.save(equipamentoSalvo);
        eventPublisher.publishEvent(new RastreabilidadeEquipamentoEvent(equipamentoSalvo));
        return new EquipamentoOutputDTO(equipamentoSalvo.getId(), equipamentoSalvo.getNome(),
                equipamentoSalvo.getSigla(), equipamentoSalvo.getDescricao());
    }

    @Transactional
    public EquipamentoOutputDTO atualizarEquipamento(Long id, EquipamentoRequestDTO equipamento) {
        Equipamento equipamentoExistente = equipamentoRepository.findById(id)
                .orElseThrow(() -> new EquipamentoNotValidException("Equipamento com id '" + id + "' não encontrado."));

        boolean nomeFoiAlterado = !equipamentoExistente.getNome().equals(equipamento.nome());

        if (nomeFoiAlterado && equipamentoRepository.checarSeNomeAchatadoExiste(equipamento.getNomeAchatado())) {
            throw new EquipamentoNotValidException("Equipamento com nome '" + equipamento.nome() + "' já existe.");
        }

        equipamentoExistente.setNome(equipamento.nome());
        equipamentoExistente.setSigla(equipamento.sigla());
        equipamentoExistente.setDescricao(equipamento.descricao());
        equipamentoExistente.setDataAtivacao(equipamento.dataAtivacao());
        equipamentoExistente.alterarCapacidade(equipamento.capacidade());

        equipamentoRepository.save(equipamentoExistente);

        eventPublisher.publishEvent(new RastreabilidadeEquipamentoEvent(equipamentoExistente));
        return new EquipamentoOutputDTO(equipamentoExistente.getId(), equipamentoExistente.getNome(),
                equipamentoExistente.getSigla(), equipamentoExistente.getDescricao());
    }

    @Transactional
    public EquipamentoOutputDTO atualizarPeso(Long id, EquipamentoPesoUpdateDTO dto) {
        Equipamento equipamentoExistente = equipamentoRepository.findById(id)
                .orElseThrow(() -> new EquipamentoNotValidException("Equipamento com id '" + id + "' não encontrado."));

       Long capacidadeAntiga = equipamentoExistente.getCapacidade();
        equipamentoExistente.alterarCapacidade(dto.capacidade());

        eventPublisher.publishEvent(new EquipamentoAtualizadoEvent(equipamentoExistente, capacidadeAntiga));

        equipamentoRepository.save(equipamentoExistente);
        return EquipamentoOutputDTO.fromEntity(equipamentoExistente);

    }

    @Transactional
    public void deletarEquipamento(Long id) {
        Equipamento equipamentoExistente = equipamentoRepository.findById(id)
                .orElseThrow(() -> new EquipamentoNotValidException("Equipamento com id '" + id + "' não encontrado."));
        Boolean possuiProgramacaoAtiva = equipamentoExistente.getProgramacao().stream()
                .anyMatch(it -> it.getStatus() == StatusProgramacao.EM_EXECUCAO
                        || it.getStatus() == StatusProgramacao.PROGRAMADA);

        if (possuiProgramacaoAtiva) {
            throw new ProgramacaoNotValidException("Ainda há programações com status de PROGRAMADO ou EM_EXECUCAO");
        }

        if (equipamentoExistente.isAtivo()) {
            equipamentoExistente.desativarEquipamento();
            eventPublisher.publishEvent(new EquipamentoAtivacaoEvent(equipamentoExistente));
        }

        eventPublisher.publishEvent(new RastreabilidadeEquipamentoEvent(equipamentoExistente));
        equipamentoRepository.delete(equipamentoExistente);
    }

    @Transactional
    public void desativarEquipamento(Long id) {
        Equipamento equipamentoExistente = equipamentoRepository.findById(id)
                .orElseThrow(() -> new EquipamentoNotValidException("Equipamento com id '" + id + "' não encontrado."));

        Boolean possuiProgramacaoAtiva = equipamentoExistente.getProgramacao().stream()
                .anyMatch(it -> it.getStatus() == StatusProgramacao.EM_EXECUCAO
                        || it.getStatus() == StatusProgramacao.PROGRAMADA);

        if (possuiProgramacaoAtiva) {
            throw new ProgramacaoNotValidException("Ainda há programações com status de PROGRAMADO ou EM_EXECUCAO");
        }

        if (!equipamentoExistente.isAtivo()) {
            throw new EquipamentoNotValidException("Equipamento já está inativo ");
        }

        equipamentoExistente.desativarEquipamento();
        eventPublisher.publishEvent(new EquipamentoAtivacaoEvent(equipamentoExistente));
    }

}
