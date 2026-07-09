package com.example.mes.producao.lote.domain.strategy;

import java.util.List;

import org.springframework.stereotype.Component;

import com.example.mes.producao.equipamento.model.Equipamento;
import com.example.mes.producao.lote.domain.Lote;
import com.example.mes.producao.lote.infraestructure.persistence.LoteRepository;
import com.example.mes.producao.ordemproducao.domain.OrdemProducao;

@Component
public class LotePadraoEstrategia implements EstrategiaCriacaoLote {

 private final LoteRepository loteRepository;

 public LotePadraoEstrategia(LoteRepository loteRepository) {
        this.loteRepository = loteRepository;
    }
    
    @Override
    public boolean deveFracionar(Equipamento eq, Long quantidade) {
        return eq.getCapacidade() > quantidade;
    }

    @Override
    public List<Lote> executar(OrdemProducao op, Lote lote, Long quantidade, Long capacidade) {
      
       Lote loteFilho = lote.gerarFilhoParaProgramacao(op, quantidade, gerarNome(lote.getNome()));
        return List.of(loteFilho);
    }



     private String gerarNome(String nome) {

        String nomeAtual = nome;

                do  {
                        char prefixo = nomeAtual.charAt(0);

                        String parteNumericaTexto = nomeAtual.substring(1);

                        int quantidadeDeDigitos = parteNumericaTexto.length();

                        long numeros = Long.parseLong(parteNumericaTexto);
                        numeros++;

                        String formato = "%0" + quantidadeDeDigitos + "d";

                        nomeAtual = prefixo + String.format(formato, numeros);
                }
                while(loteRepository.existsByNome(nomeAtual));

                return nomeAtual;
        
    }

}
