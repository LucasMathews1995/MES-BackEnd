package com.example.mes.producao.application.mapper;



import com.example.mes.producao.application.dto.OrdemProducaoResponseDTO;
import com.example.mes.producao.domain.OrdemProducao;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;



@Component
@RequiredArgsConstructor
public class OrdemProducaoMapper {



    public OrdemProducaoResponseDTO toDTO (OrdemProducao ordemProducao) {
        return new OrdemProducaoResponseDTO(ordemProducao.getNumeroOP(),ordemProducao.getOrdemVendaId(),ordemProducao.getStatus(),ordemProducao.getDataCriacao(),ordemProducao.getDataEncerramento());
    }

}
