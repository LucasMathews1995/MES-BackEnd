package com.example.mes.producao.application.service;


import com.example.mes.producao.api.exception.NotFoundLoteException;
import com.example.mes.producao.api.exception.ProgramacaoNotFoundException;
import com.example.mes.producao.domain.Lote;
import com.example.mes.producao.domain.Programacao;
import com.example.mes.producao.infraestructure.LoteRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class LoteDataService {

    private final LoteRepository loteRepository;

    private final Random random = new Random();
    private final OrdemProducaoService ordemProducaoService ;

        //C
    @Transactional
    public void salvarLote(Lote lote) {
    loteRepository.save(lote);
    }
    //R
    public Lote buscarLotePorId(long id){
        return  loteRepository.findById(id).orElseThrow(()-> new NotFoundLoteException("Nenhum lote foi encontrado com esse id "+ id));
    }
    public List<Lote> findAllLotes(){
        return    loteRepository.findAll();

    }
    public List<Lote> buscarTodosSemOrdemProducao(){
        return  loteRepository.findAll().stream().filter(it -> it.getOrdemProducao()==null).toList();
    }


    @Transactional
    public void deleteLoteById(Long id){

        loteRepository.deleteById(id);

    }
    public Programacao buscarUltimaProgramacaoPorLote(Long loteId){
        return loteRepository.buscarUltimaProgramacaoPorLote(loteId).orElseThrow(()-> new ProgramacaoNotFoundException("Nenhuma programacao foi encontrado com esse id "+ loteId));
    }
    public boolean existirPorNome(String nome){
        return loteRepository.existsByNome(nome);

    }



    }

