package com.example.mes.rastreabilidade.domain.event;

import com.example.mes.producao.equipamento.model.Equipamento;

public record EquipamentoAtualizadoEvent(Equipamento equipamento,Long capacidadeAntiga) {

}
