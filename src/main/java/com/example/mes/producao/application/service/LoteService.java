package com.example.mes.producao.application.service;

import com.example.mes.producao.api.exception.*;
import com.example.mes.producao.application.dto.LoteRequestDTO;
import com.example.mes.producao.application.mapper.LoteMapper;
import com.example.mes.producao.domain.*;
import com.example.mes.producao.infraestructure.LoteRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class LoteService {
    private final LoteRepository loteRepository;
    private final LoteMapper loteMapper;
    private final Random random = new Random();


    @Transactional
    public Lote salvarLote(Lote lote) {
       return loteRepository.save (lote);

    }
    //R
    public Lote buscarLotePorId(Long id){
        return  loteRepository.findById(id).orElseThrow(()-> new NotFoundLoteException("Nenhum lote foi encontrado com esse id "+ id));
    }
    public List<Lote> findAllLotes(){
        return    loteRepository.findAll();

    }
    public List<Lote> buscarTodosSemOrdemProducao(){
        return  loteRepository.findAll().stream().filter(it -> it.getOrdemProducao()==null).toList();
    }


    public Programacao buscarUltimaProgramacaoPorLote(Long loteId){
        return loteRepository.buscarUltimaProgramacaoPorLote(loteId).orElseThrow(()-> new ProgramacaoNotFoundException("Nenhuma programacao foi encontrado com esse id "+ loteId));
    }


    @Transactional
    public Lote criarLote(LoteRequestDTO dto){

        Lote lote =  loteMapper.toEntity(dto);
        lote.setNome(generateLoteNome());
        lote.setStatus(StatusLote.DESABASTECIDO);

        loteRepository.save(lote);


        return lote;

    }


    @Transactional
    public Lote abastecerLote(Long id ){
        Lote lote = buscarLotePorId(id);

        validarTransicaoAbastecimento(lote.getStatus(),StatusLote.ABASTECIDO);

        lote.setStatus(StatusLote.ABASTECIDO);

        return loteRepository.save(lote);
    }

    @Transactional
    public Lote programarLote(Long id ){
        Lote lote = buscarLotePorId(id);

        validarTransicaoAbastecimento(lote.getStatus(),StatusLote.PROGRAMADO);

        lote.setStatus(StatusLote.PROGRAMADO);

        return loteRepository.save(lote);
    }



    @Transactional
    public Lote produzirLote(Long id) {
        Lote lote = buscarLotePorId(id);

        validarTransicaoAbastecimento(lote.getStatus(), StatusLote.PRODUZIDO);
        lote.setStatus(StatusLote.PRODUZIDO);

    return loteRepository.save(lote);
    }

    @Transactional
    public Lote aprovarLote(Long id ){
        Lote lote =  buscarLotePorId(id);

        validarTransicaoAbastecimento(lote.getStatus(),StatusLote.APROVADO);
        lote.setStatus(StatusLote.APROVADO);

        return loteRepository.save(lote);


    }



    @Transactional
    public Lote colocarLoteEmQualidade(Long id,Integer quantidade ){

        Lote lote =    buscarLotePorId(id);

       if(lote.getStatus() == StatusLote.APROVADO || lote.getStatus() == StatusLote.PRODUZIDO){
           throw new QualityException("O lote não pode ser colocado em qualidade pois seu status é de : " + lote.getStatus());
       }

       lote.setStatus(StatusLote.QUALIDADE);
       lote.retornarQuantidade(quantidade);

        OrdemProducao ordem = lote.getOrdemProducao();

        if(ordem != null){
            ordem.removeLote(lote);
        }
       return loteRepository.save(lote);
    }


    @Transactional
    public void retirarLoteEmQualidade(Long id ){
        Lote lote =    buscarLotePorId(id);

        if(!lote.getStatus().equals(StatusLote.QUALIDADE)){
            throw new QualityException("O lote não pode ser retirado em qualidade pois seu status é de : " + lote.getStatus() + " e ele precisa estar em qualidade");
        }

        lote.setStatus(StatusLote.DESABASTECIDO);

        loteRepository.save(lote);

    }

    @Transactional
    public void excluirLote(Long id) {
        Lote lote = buscarLotePorId(id);
        List<Programacao> listaLote = lote.getProgramacao().stream().filter(it-> it.getStatus().equals(StatusProgramacao.PROGRAMADO) || it.getStatus().equals(StatusProgramacao.CRIADO)).toList();


        if (!lote.getStatus().equals(StatusLote.DESABASTECIDO)) {
            throw new NotFoundLoteException("para excluir o lote precisa estar como desabastecido, porém ele está com o seguinte status " + lote.getStatus());
        }else if(!listaLote.isEmpty()){
            throw new LoteAbastecidoException("Lote não pode ser excluir, pois ele está :" + lote.getStatus());
        }


        loteRepository.deleteById(id);

    }


    private void validarTransicaoAbastecimento(StatusLote atual, StatusLote novo) {

        boolean permitida = switch (atual) {
            case DESABASTECIDO -> novo == StatusLote.ABASTECIDO;
            case ABASTECIDO ->  novo == StatusLote.PRODUZIDO ;
            case PRODUZIDO -> novo == StatusLote.APROVADO;

            default -> false;
        };

        if (!permitida) {
            throw new AbastecimentoLoteException("Transição não permitida: " + atual + " -> " + novo);
        }
    }


    private String generateLoteNome(){
        char prefixo = 'A';
        int numeroBase =  random.nextInt(9000);
        String nome =String.format("%s%04d%s", prefixo, numeroBase, "0100");
        while(loteRepository.existsByNome(nome)){
            numeroBase = random.nextInt(9000) ;
            prefixo++;
            nome = String.format("%s%04d%s", prefixo, numeroBase, "0100");
        }
        return nome;

    }







}
