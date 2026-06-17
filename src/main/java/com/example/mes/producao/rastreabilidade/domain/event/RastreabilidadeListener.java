package com.example.mes.producao.rastreabilidade.domain.event;

import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import com.example.mes.producao.equipamento.model.StatusEquipamento;
import com.example.mes.producao.programacao.domain.StatusProgramacao;
import com.example.mes.producao.rastreabilidade.domain.Rastreabilidade;
import com.example.mes.producao.rastreabilidade.domain.StatusRastreabilidade;
import com.example.mes.producao.rastreabilidade.domain.exceptions.NotFoundStatusRastreabilidadeException;
import com.example.mes.producao.rastreabilidade.infraestructure.persistence.RastreabilidadeRepository;

@Component
public class RastreabilidadeListener {

    private final RastreabilidadeRepository rastreabilidadeRepository;

    public RastreabilidadeListener(RastreabilidadeRepository rastreabilidadeRepository) {
        this.rastreabilidadeRepository = rastreabilidadeRepository;
    }

    @EventListener
    public void rastrearProgramacao(RastreabilidadeEvent event) {
        StatusRastreabilidade statusRastreabilidade  = mudarRastreabilidade(event.programacao().getStatus());

        Rastreabilidade evento = Rastreabilidade.criarEventoLote(statusRastreabilidade,event.programacao().getLoteConsumido(),
        event.programacao().getLoteProduzido(),
         event.programacao().getEquipamento());

        rastreabilidadeRepository.save(evento);


    }
    @EventListener
    public void rastrearEquipamento(RastreabilidadeEquipamentoEvent event){
        StatusRastreabilidade statusRastreabilidade = mudarRastreabilidadeEquipamento(event.equipamento().getStatusEquipamento());


        Rastreabilidade evento  = Rastreabilidade.equipamentoRastreabilidade(event.equipamento(), statusRastreabilidade);
        rastreabilidadeRepository.save(evento);
    }

    private StatusRastreabilidade mudarRastreabilidadeEquipamento(StatusEquipamento statusEquipamento) {
		  return switch (statusEquipamento) {
    case  OPERANDO    -> StatusRastreabilidade.OPERANDO;
    case PARADO -> StatusRastreabilidade.PARADA;
      default -> throw new NotFoundStatusRastreabilidadeException(
        "Não há nenhum status compatível para esse Status: " + statusEquipamento
    );
};
	}

	private StatusRastreabilidade mudarRastreabilidade(StatusProgramacao status){

     return switch (status) {
    case  CRIADA    -> StatusRastreabilidade.CRIADO;
    case PROGRAMADA -> StatusRastreabilidade.PROGRAMADO;
    case EM_EXECUCAO -> StatusRastreabilidade.EM_EXECUCAO;
    case CONCLUIDA     -> StatusRastreabilidade.CONCLUIDO;
    default -> throw new NotFoundStatusRastreabilidadeException(
        "Não há nenhum status compatível para esse Status: " + status
    );
};
    }

  
}


