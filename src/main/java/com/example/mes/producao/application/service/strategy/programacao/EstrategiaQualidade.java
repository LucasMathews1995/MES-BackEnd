package com.example.mes.producao.application.service.strategy.programacao;

import com.example.mes.producao.application.service.LoteService;
import com.example.mes.producao.application.service.ProgramacaoService;
import com.example.mes.producao.application.service.RastreabilidadeService;
import com.example.mes.producao.domain.Equipamento;
import com.example.mes.producao.domain.Lote;
import com.example.mes.producao.domain.Programacao;
import com.example.mes.producao.domain.StatusProgramacao;
import com.example.mes.producao.domain.StatusRastreabilidade;

import java.time.LocalDateTime;

import org.springframework.stereotype.Component;

@Component
public class EstrategiaQualidade implements MudancaStatusStrategy{
    @Override
    public StatusProgramacao getStatusAlvo() {
        return StatusProgramacao.QUALIDADE;
    }

    @Override
    public Lote processarLote(LoteService loteService, Long LoteId) {
     return   loteService.colocarLoteEmQualidade(LoteId);
    }

    @Override
    public void finalizarProgramacao(Programacao programacao ) {
      programacao.colocarEmQualidade();
    }

    @Override
    public boolean permiteTransicao(StatusProgramacao statusAtual) {
        return  statusAtual != StatusProgramacao.PRODUZIDO && statusAtual != StatusProgramacao.APROVADO;
    }

    @Override
    public void registrarRastreabilidade(Lote lote, Equipamento equipamento,
            RastreabilidadeService rastreabilidadeService) {

   String evento = "O lote " + lote.getNome() + "foi colocado em qualidade no equipamento " + equipamento.getNome() + " em " + LocalDateTime.now();
        rastreabilidadeService.registrarEventoRastreabilidade(lote, equipamento, StatusRastreabilidade.QUALIDADE, evento);
    }
    
}
