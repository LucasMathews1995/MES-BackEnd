package com.example.mes.producao.application.service;

import com.example.mes.producao.api.exception.NotFoundLoteException;
import com.example.mes.producao.api.exception.OrdemAndLoteException;
import com.example.mes.producao.api.exception.OrdemProducaoNotFoundException;
import com.example.mes.producao.api.exception.OrdemProducaoStatusException;
import com.example.mes.producao.domain.Lote;
import com.example.mes.producao.domain.OrdemProducao;
import com.example.mes.producao.domain.StatusLote;
import com.example.mes.producao.domain.StatusOP;
import com.example.mes.producao.infraestructure.OrdemProducaoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class OrdemProducaoService {

    private final OrdemProducaoRepository ordemProducaoRepository;
    private final Random random = new Random();

    public OrdemProducao buscarPorId(Long id) {
        return ordemProducaoRepository.findById(id).orElseThrow(
                () -> new OrdemProducaoNotFoundException("Não há ordem de produção para essa ordem " + id));
    }

    public List<OrdemProducao> buscarTodasOrdemProducao() {
        List<OrdemProducao> op = ordemProducaoRepository.findAll();

        if (op.isEmpty()) {
            throw new OrdemProducaoNotFoundException("Ordens de Produção não encontradas ");
        }
        return op;
    }
    

    public OrdemProducao vincularLote(Long idProd, Lote lote) {
        OrdemProducao ordem = buscarPorId(idProd);

        if (ordem.getStatus() == StatusOP.FINALIZADA) {
            throw new OrdemProducaoStatusException("Essa ordem de produção " + ordem.getNumeroOP()
                    + " já está finalizada, não pode vinculá-la a um lote");
        }

        ordem.addLote(lote);
        ordem.setStatus(StatusOP.PROCESSANDO);

        return ordem;
    }

    public List<Lote> buscarLotesPorOrdemProducao(Long id) {
        OrdemProducao op = buscarPorId(id);

        List<Lote> lotes = op.getLotes().stream().toList();

        if (lotes.isEmpty()) {
            throw new NotFoundLoteException("Lotes não encontrados para essa ordem: " + id);
        }

        return lotes;
    }

    @Transactional
    public OrdemProducao createOrdemProducao() {
        String numeroUnico = generateOrdemProducaoNome();
        OrdemProducao novaOP = new OrdemProducao(numeroUnico);

        return ordemProducaoRepository.save(novaOP);
    }

    @Transactional
    public void deletarOrdemProducao(Long idProd) {
        OrdemProducao ordem = buscarPorId(idProd);

        ordem.validarSePodeSerExcluido();

        ordemProducaoRepository.delete(ordem);

    }

    @Transactional
    public void vincularOrdemVenda(Long idProd, Long idVenda) {
        OrdemProducao ordem = buscarPorId(idProd);

        ordem.setOrdemVendaId(idVenda);

        ordemProducaoRepository.save(ordem);

    }

    @Transactional
    public void desvincularLote(Long idLote, Long idProd) {
        OrdemProducao ordem = buscarPorId(idProd);

        ordem.desvincularLote(idLote);

        ordemProducaoRepository.save(ordem);

    }

    @Transactional

    public void deleteOrdemVenda(Long idProd) {
        OrdemProducao ordem = buscarPorId(idProd);

        ordem.setOrdemVendaId(null);

        ordemProducaoRepository.save(ordem);
    }

    private String generateOrdemProducaoNome() {
        String prefixo = "OP";
        int numeroBase = random.nextInt(1_000_000, 9_999_999);
        return String.format(prefixo + numeroBase);

    }

}
