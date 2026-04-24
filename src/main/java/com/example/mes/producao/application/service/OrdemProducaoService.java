package com.example.mes.producao.application.service;


import com.example.mes.producao.api.exception.NotFoundLoteException;
import com.example.mes.producao.api.exception.OrdemAndLoteException;
import com.example.mes.producao.api.exception.OrdemProducaoNotFoundException;
import com.example.mes.producao.application.mapper.OrdemProducaoMapper;
import com.example.mes.producao.domain.Lote;
import com.example.mes.producao.domain.OrdemProducao;
import com.example.mes.producao.domain.StatusLote;
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
        return ordemProducaoRepository.findById(id).orElseThrow(() -> new OrdemProducaoNotFoundException("Não há ordem de produção para essa ordem " + id));
    }

    public List<OrdemProducao> buscarTodasOrdemProducao() {
        List<OrdemProducao> op =  ordemProducaoRepository.findAll();

        if(op.isEmpty()){
            throw new OrdemProducaoNotFoundException("Ordens de Produção não encontradas ");
        }
        return op;
    }


    public List<Lote> buscarLotesPorOrdemProducao(Long id) {
        OrdemProducao op = buscarPorId(id);

       List<Lote> lotes =  op.getLotes().stream().toList();

       if(lotes.isEmpty()){
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


        Optional<Lote> lote = ordem.getLotes().stream()
                .filter(it -> (it.getStatus().equals(StatusLote.ABASTECIDO)))
                .findAny();

        if (lote.isPresent()) {
            throw new OrdemAndLoteException("Ainda há lotes abastecidos a serem desvinculados do processo .Precisa desabastecer.");
        }

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

        Optional<Lote> lote = ordem.getLotes().stream()
                .filter(it -> it.getId().equals(idLote) && (it.getStatus().equals(StatusLote.QUALIDADE) || it.getStatus().equals(StatusLote.APROVADO)))
                .findAny();

        if (lote.isPresent()) {
            ordem.getLotes().forEach(ordem::removeLote);
        } else if (!ordem.getLotes().isEmpty()) {
            throw new OrdemAndLoteException("O lote está somente abastecido ou produzido na OP . Portanto não pode desvincular, coloque em qualidade ou aprove-o para conseguir");
        }

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
