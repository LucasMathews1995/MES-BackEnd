package com.example.mes.producao.application.service;

import com.example.mes.producao.api.exception.OrdemProducaoNotFoundException;
import com.example.mes.producao.application.dto.OrdemProducaoResponseDTO;
import com.example.mes.producao.application.mapper.OrdemProducaoMapper;
import com.example.mes.producao.domain.OrdemProducao;
import com.example.mes.producao.domain.StatusOP;
import com.example.mes.producao.infraestructure.LoteRepository;
import com.example.mes.producao.infraestructure.OrdemProducaoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import java.util.Random;

@Service
@RequiredArgsConstructor
public class OrdemProducaoService {

    private final OrdemProducaoRepository ordemProducaoRepository;
    private final OrdemProducaoMapper ordemProducaoMapper;
    private final LoteRepository loteRepository;

    private final Random random= new Random();



    @Transactional
    public OrdemProducaoResponseDTO createOrdemProducao( ) {

      OrdemProducao ordem =   ordemProducaoRepository.save(new OrdemProducao());
      ordem.setNumeroOP(generateOrdemProducaoNome());
        OrdemProducao producaoSalva =  ordemProducaoRepository.save(ordem);
        return ordemProducaoMapper.toDTO(producaoSalva);

    }
    public OrdemProducaoResponseDTO findDtoById(Long id ) {
        OrdemProducao ordem =  getEntityById(id);
        return ordemProducaoMapper.toDTO(ordem);

    }

    public List<OrdemProducaoResponseDTO> getAllOrdemProducao() {
        List<OrdemProducao> ordemProducao = ordemProducaoRepository.findAll();
        return ordemProducao.stream().map(ordemProducaoMapper::toDTO).toList();
    }

    @Transactional
    public void vincularOrdemVenda(Long idProd,Long idVenda){
        OrdemProducao ordem = getEntityById(idProd);

        ordem.setOrdemVendaId(idVenda);

        ordemProducaoRepository.save(ordem);

    }

    @Transactional
    public void deleteOrdemVenda(Long idProd){
        OrdemProducao ordem = getEntityById(idProd);

        ordem.setOrdemVendaId(null);

        ordemProducaoRepository.save(ordem);
    }

    @Transactional
    public void deleteOrdemProducao(Long idProd){
        OrdemProducao ordem = getEntityById(idProd);

        ordemProducaoRepository.delete(ordem);

    }

    @Transactional
    public  void atualizarStatus(Long id,  StatusOP status){
        OrdemProducao ordem = getEntityById(id);
        ordem.setStatus(status);
        ordemProducaoRepository.save(ordem);

    }






    public    OrdemProducao getEntityById(long id){
     return ordemProducaoRepository.findById(id).orElseThrow(()-> new OrdemProducaoNotFoundException("Nenhuma ordem de producao encontrada com esse id "+ id));
    }


    private String generateOrdemProducaoNome(){
        String prefixo  = "OP";
        int numeroBase = random.nextInt(999999);
        String nome = String.format(prefixo + numeroBase );
        while(ordemProducaoRepository.existsByNumeroOP(nome)){
           numeroBase = random.nextInt(999999);
            nome = String.format(prefixo + numeroBase );
        }
        return nome;
    }
}
