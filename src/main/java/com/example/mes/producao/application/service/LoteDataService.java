package com.example.mes.producao.application.service;


import com.example.mes.producao.api.exception.NotFoundLoteException;
import com.example.mes.producao.domain.Lote;
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

    public Lote saveLote(Lote lote) {
       return  loteRepository.save(lote);
    }
    //R
    public Lote buscarPorId(long id){
        return  loteRepository.findById(id).orElseThrow(()-> new NotFoundLoteException("Nenhum lote foi encontrado com esse id "+ id));
    }
    public List<Lote> findAllLotes(){
        return    loteRepository.findAll();

    }
    public void deleteLote(long id){
        loteRepository.deleteById(id);
    }


















    @Transactional
    public void deleteLoteById(Long id){
        if(!loteRepository.existsById(id)){
    throw new NotFoundLoteException("lote com ID " + id + " não encontrado");
        }

        loteRepository.deleteById(id);


    }










    }

