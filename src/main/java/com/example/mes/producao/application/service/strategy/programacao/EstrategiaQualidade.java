package com.example.mes.producao.application.service.strategy.programacao;

import com.example.mes.producao.application.dto.ProgramacaoRequestDTO;
import com.example.mes.producao.application.service.LoteService;
import com.example.mes.producao.application.service.ProgramacaoService;
import com.example.mes.producao.domain.Lote;
import com.example.mes.producao.domain.Programacao;
import com.example.mes.producao.domain.StatusProgramacao;
import org.springframework.stereotype.Component;

@Component
public class EstrategiaQualidade implements MudancaStatusStrategy{
    @Override
    public StatusProgramacao getStatusAlvo() {
        return StatusProgramacao.QUALIDADE;
    }

    @Override
    public Lote processarLote(LoteService loteService, ProgramacaoRequestDTO dto) {
     return   loteService.colocarLoteEmQualidade(dto.loteId(),dto.quantidadeConsumida());
    }

    @Override
    public void finalizarProgramacao(Programacao programacao, ProgramacaoService programacaoService) {
        programacaoService.deletarProgramacaoPorId(programacao.getId());
    }

    @Override
    public boolean permiteTransicao(StatusProgramacao statusAtual) {
        return  statusAtual != StatusProgramacao.PRODUZIDO && statusAtual != StatusProgramacao.APROVADO;
    }
}
