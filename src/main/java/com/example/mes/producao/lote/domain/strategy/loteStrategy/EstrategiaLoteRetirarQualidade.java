package com.example.mes.producao.lote.domain.strategy.loteStrategy;

import org.springframework.stereotype.Component;

import com.example.mes.producao.lote.domain.Lote;
import com.example.mes.producao.lote.domain.StatusLote;
import com.example.mes.producao.lote.infraestructure.persistence.LoteRepository;
import com.example.mes.producao.programacao.domain.Programacao;

@Component
public class EstrategiaLoteRetirarQualidade implements EstrategiaLote {
    private final LoteRepository loteRepository;

    public EstrategiaLoteRetirarQualidade(LoteRepository loteRepository){
        this.loteRepository = loteRepository;
    }

    @Override
    public StatusLote getStatusAlvo() {
        return StatusLote.LIBERADO;
    }

    @Override
    public void processar(Programacao programacao) {


        Lote lote  = programacao.getLoteConsumido();
     Lote loteProduzido=    lote.retirarDaQualidadeConsumido(gerarNome(lote.getNome()));
     
     programacao.setLoteProduzido(loteProduzido);
       
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
