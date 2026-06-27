package com.example.mes.producao.lote.domain.strategy;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.example.mes.producao.equipamento.model.Equipamento;
import com.example.mes.producao.lote.domain.Lote;
import com.example.mes.producao.lote.infraestructure.persistence.LoteRepository;
import com.example.mes.producao.ordemproducao.domain.OrdemProducao;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class FracionamentoPorCapacidadeStrategy implements EstrategiaCriacaoLote{
    private final LoteRepository loteRepository;

    @Override
    public boolean deveFracionar(Equipamento eq, int quantidade) {
       return eq.getCapacidade() < quantidade;
    }

    @Override
    public List<Lote> executar(OrdemProducao op, Lote lotePai, int quantidade,int capacidade) {
       

int qteLotes = quantidade / capacidade;
        int restante = quantidade % capacidade;
        List<Lote> lotes = new ArrayList<>();
        String ultimoNome = lotePai.getNome();

        for (int i = 0; i < qteLotes; i++) {
            ultimoNome = gerarNome(ultimoNome);
            lotes.add(lotePai.gerarFilhoParaProgramacao(op, capacidade, ultimoNome));
        }

        if (restante > 0) {
            ultimoNome = gerarNome(ultimoNome);
            lotes.add(lotePai.gerarFilhoParaProgramacao(op, restante, ultimoNome));
        }
        return lotes;
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
