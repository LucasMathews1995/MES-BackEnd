package com.example.mes.producao.rastreabilidade.domain.event;




import com.example.mes.producao.programacao.domain.Programacao;



    
public record RastreabilidadeEvent(
    Programacao programacao 
) {

}
