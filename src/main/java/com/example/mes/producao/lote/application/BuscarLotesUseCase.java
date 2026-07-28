package com.example.mes.producao.lote.application;


import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import com.example.mes.producao.lote.domain.Lote;
import com.example.mes.producao.lote.domain.exceptions.LoteWithoutOrdemException;
import com.example.mes.producao.lote.domain.exceptions.NotFoundLoteException;
import com.example.mes.producao.lote.infraestructure.dto.LoteOutputDTO;
import com.example.mes.producao.lote.infraestructure.persistence.LoteRepository;


@Service
public class BuscarLotesUseCase {


    private final LoteRepository loteRepository;


    public BuscarLotesUseCase(LoteRepository loteRepository){
        this.loteRepository = loteRepository;
    }


    public List<LoteOutputDTO> buscarLotesSemOrdemProducao (){
         List<Lote> lote = loteRepository.buscarSemOrdemProducao().orElseThrow(()-> new NotFoundLoteException("Nenhum lote encontrado"));
        

        return lote.stream().map(LoteOutputDTO::fromEntity).collect(Collectors.toList());
    }

    public LoteOutputDTO buscarLotesPorId(Long id){
        Lote lote = loteRepository.findById(id).orElseThrow(()-> new NotFoundLoteException("Nenhum lote encontrado com esse id :" + id));

        return LoteOutputDTO.fromEntity(lote);

    }

    public List<LoteOutputDTO> buscarLotesPorOrdemPorducao (Long ordemProducaoId){
    List<Lote> lote = loteRepository.buscarPorOrdemProducao(ordemProducaoId).orElseThrow(()-> new NotFoundLoteException("Nenhum lote encontrado com esse id da Ordem de Producao :" + ordemProducaoId));
        if(lote.isEmpty()){
            throw new LoteWithoutOrdemException("Nenhum lote encontrado com esse id da Ordem de Producao :" + ordemProducaoId);
        }

    return lote.stream()
    .map(LoteOutputDTO::fromEntity)
    .collect(Collectors.toList());


          
    }

    



}
