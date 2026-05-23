package com.example.mes.producao.application.service.strategy.lote;

import com.example.mes.producao.domain.StatusLote;

public interface EstrategiaLote  {
    StatusLote getStatusAlvo();
    void aplicar(Long idLote);
    void permitirTransicao(Long idLote);
    void atualizarStatusLote(Long idLote);

}
