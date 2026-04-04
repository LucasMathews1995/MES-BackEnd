package com.example.mes.producao.application.service;

import com.example.mes.producao.api.exception.AbastecimentoLoteException;
import com.example.mes.producao.api.exception.LoteAbastecidoException;
import com.example.mes.producao.api.exception.NotFoundLoteException;
import com.example.mes.producao.api.exception.QualityException;
import com.example.mes.producao.application.dto.LoteRequestDTO;
import com.example.mes.producao.application.dto.LoteResponseDTO;
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
public class LoteProcessamentoService {

    private final LoteDataService loteDataService;
    private final LoteMapper loteMapper;
    private final Random random = new Random();
    private final ProgramacaoService programacaoService;



    @Transactional
    public Lote criarLote(LoteRequestDTO dto){

        Lote lote =  loteMapper.toEntity(dto);
        lote.setNome(generateLoteNome());
        lote.setStatus(StatusLote.DESABASTECIDO);

        loteDataService.salvarLote(lote);


        return lote;

    }

    @Transactional
    public void desabastecerLote(Long id) {

        Lote lote = loteDataService.buscarLotePorId(id);
       Programacao programacao  =   loteDataService.buscarUltimaProgramacaoPorLote(id);

        if (!lote.getStatus().equals(StatusLote.ABASTECIDO)) {
            throw new AbastecimentoLoteException("Lote " + lote.getNome() + " não está abastecido");
        }

        lote.setStatus(StatusLote.DESABASTECIDO);


        programacaoService.deletarProgramacaoPorId(programacao.getId());
        lote.removerProgramacao(programacao);

        loteDataService.salvarLote(lote);


    }



    @Transactional
    public void abastecerLote(Long id ){
        Lote lote = loteDataService.buscarLotePorId(id);

        validarTransicaoAbastecimento(lote.getStatus(),StatusLote.ABASTECIDO);

        lote.setStatus(StatusLote.ABASTECIDO);




    }
    @Transactional
    public void produzirLote(Long id ){
    Lote lote =    loteDataService.buscarLotePorId(id);

        validarTransicaoAbastecimento(lote.getStatus(),StatusLote.PRODUZIDO);
        lote.setStatus(StatusLote.PRODUZIDO);



    }
    @Transactional
    public void aprovarLote(Long id ){
        Lote lote =    loteDataService.buscarLotePorId(id);

        validarTransicaoAbastecimento(lote.getStatus(),StatusLote.APROVADO);
        lote.setStatus(StatusLote.APROVADO);


    }



    @Transactional
    public void colocarLoteEmQualidade(Long id,Integer quantidade ){

        Lote lote =    loteDataService.buscarLotePorId(id);

       if(lote.getStatus() == StatusLote.APROVADO || lote.getStatus() == StatusLote.PRODUZIDO){
           throw new QualityException("O lote não pode ser colocado em qualidade pois seu status é de : " + lote.getStatus());
       }

       lote.setStatus(StatusLote.QUALIDADE);
       lote.retornarQuantidade(quantidade);



        OrdemProducao ordem = lote.getOrdemProducao();

        if(ordem != null){
            ordem.removeLote(lote);
        }

    }


    @Transactional
    public void retirarLoteEmQualidade(Long id ){
        Lote lote =    loteDataService.buscarLotePorId(id);

        if(!lote.getStatus().equals(StatusLote.QUALIDADE)){
            throw new QualityException("O lote não pode ser retirado em qualidade pois seu status é de : " + lote.getStatus() + " e ele precisa estar em qualidade");
        }


        lote.setStatus(StatusLote.DESABASTECIDO);

    }

    @Transactional
    public void excluirLote(Long id) {
        Lote lote = loteDataService.buscarLotePorId(id);
        List<Programacao> listaLote = lote.getProgramacao().stream().filter(it-> it.getStatus().equals(StatusProgramacao.PROGRAMADO) || it.getStatus().equals(StatusProgramacao.CRIADA)).toList();




        if (!lote.getStatus().equals(StatusLote.DESABASTECIDO)) {
            throw new NotFoundLoteException("para excluir o lote precisa estar como desabastecido, porém ele está com o seguinte status " + lote.getStatus());
        }else if(!listaLote.isEmpty()){
            throw new LoteAbastecidoException("Lote não pode ser excluir, pois ele está :" + lote.getStatus());
        }


        loteDataService.deleteLoteById(id);

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
        while(loteDataService.existirPorNome(nome)){
            numeroBase = random.nextInt(9000) ;
            prefixo++;
            nome = String.format("%s%04d%s", prefixo, numeroBase, "0100");
        }
        return nome;

    }







}
