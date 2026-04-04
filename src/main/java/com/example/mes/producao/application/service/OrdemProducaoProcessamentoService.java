package com.example.mes.producao.application.service;

import com.example.mes.producao.api.exception.AlreadyExistOrdemProducaoException;
import com.example.mes.producao.api.exception.OrdemAndLoteException;
import com.example.mes.producao.domain.Lote;
import com.example.mes.producao.domain.OrdemProducao;
import com.example.mes.producao.domain.StatusLote;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class OrdemProducaoProcessamentoService {

    private final OrdemProducaoService ordemProducaoService;
    private final LoteDataService loteDataService;
    private final Random random = new Random();






    @Transactional
    public OrdemProducao createOrdemProducao( ) {
        String numeroUnico = generateOrdemProducaoNome();
        OrdemProducao novaOP = new OrdemProducao(numeroUnico);

        return  ordemProducaoService.salvarOrdemProducao(novaOP);
    }







    @Transactional
    public OrdemProducao vincular(Long idLote, Long idProd){
        Lote lote = loteDataService.buscarLotePorId(idLote);

        if(lote.getOrdemProducao()!= null ){
            throw new AlreadyExistOrdemProducaoException("Esse lote "+ lote.getNome() + " já possui uma ordem de produção , não pode vinculá-lo " );
        }

        OrdemProducao ordemProducao = ordemProducaoService.buscarPorId(idProd);

        ordemProducao.addLote(lote);

        return ordemProducao;
    }

    @Transactional
    public void deletarOrdemProducao(Long idProd){
        OrdemProducao ordem  = ordemProducaoService.buscarPorId(idProd);



        Optional<Lote> lote = ordem.getLotes().stream()
                .filter(it-> (it.getStatus().equals(StatusLote.ABASTECIDO)))
                .findAny();

        if(lote.isPresent()){
            throw new OrdemAndLoteException("Ainda há lotes abastecidos a serem desvinculados do processo .Precisa desabastecer.");
        }

        ordemProducaoService.deletarOrdemProducao(ordem);

        }



    @Transactional
    public void vincularOrdemVenda(Long idProd,Long idVenda){
        OrdemProducao ordem = ordemProducaoService.buscarPorId(idProd);

        ordem.setOrdemVendaId(idVenda);

        ordemProducaoService.salvarOrdemProducao(ordem);

    }

    @Transactional
    public  void desvincularLote(Long idLote,  Long idProd){
        OrdemProducao ordem = ordemProducaoService.buscarPorId(idProd);

        Optional<Lote> lote = ordem.getLotes().stream()
                .filter(it-> it.getId().equals(idLote) && (it.getStatus().equals(StatusLote.QUALIDADE)||it.getStatus().equals(StatusLote.APROVADO)))
                .findAny();

        if( lote.isPresent()){
            ordem.getLotes().forEach(ordem::removeLote);
        }else if(!ordem.getLotes().isEmpty()){
            throw new OrdemAndLoteException("O lote está somente abastecido ou produzido na OP . Portanto não pode desvincular, coloque em qualidade ou aprove-o para conseguir");
        }

            ordemProducaoService.salvarOrdemProducao(ordem);

    }


    @Transactional
    public void deleteOrdemVenda(Long idProd){
        OrdemProducao ordem = ordemProducaoService.buscarPorId(idProd);

        ordem.setOrdemVendaId(null);

        ordemProducaoService.salvarOrdemProducao(ordem);
    }

    private String generateOrdemProducaoNome(){
        String prefixo  = "OP";
        int numeroBase = random.nextInt(1_000_000,9_999_999);
       return  String.format(prefixo + numeroBase );

    }
}
