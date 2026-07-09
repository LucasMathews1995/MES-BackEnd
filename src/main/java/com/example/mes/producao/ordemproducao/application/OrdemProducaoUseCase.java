package com.example.mes.producao.ordemproducao.application;

import java.util.Random;

import org.springframework.stereotype.Service;

import com.example.mes.producao.equipamento.exceptions.NotFoundEquipamentoException;
import com.example.mes.producao.equipamento.model.Equipamento;
import com.example.mes.producao.equipamento.repositories.EquipamentoRepository;
import com.example.mes.producao.lote.domain.Lote;
import com.example.mes.producao.lote.domain.exceptions.NotFoundLoteException;
import com.example.mes.producao.lote.infraestructure.persistence.LoteRepository;
import com.example.mes.producao.ordemproducao.domain.OrdemProducao;
import com.example.mes.producao.ordemproducao.dto.OrdemProducaoInputDTO;
import com.example.mes.producao.ordemproducao.dto.OrdemProducaoOuputDTO;
import com.example.mes.producao.ordemproducao.exceptions.OPNotFoundException;
import com.example.mes.producao.ordemproducao.infraestructure.persistence.OrdemProducaoRepository;

import jakarta.transaction.Transactional;

@Service
public class OrdemProducaoUseCase {


    private final OrdemProducaoRepository repository;
    private final LoteRepository loteRepository;
    private final EquipamentoRepository equipamentoRepository;
    
    public OrdemProducaoUseCase ( OrdemProducaoRepository repository, LoteRepository loteRepository, EquipamentoRepository equipamentoRepository){
        this.repository = repository;
        this.loteRepository = loteRepository;
        this.equipamentoRepository = equipamentoRepository;
    }


    @Transactional
    public OrdemProducaoOuputDTO salvarOrdemProducao(OrdemProducaoInputDTO dto){

     
     OrdemProducao op = OrdemProducao.criarNormal(dto.equipamentoId(), darNomeOP(),dto.capacidadeMaxima());
     
      if (!equipamentoRepository.existsById(dto.equipamentoId())) {
            throw new NotFoundEquipamentoException("Equipamento não encontrado");
        }

     repository.save(op);   
      return OrdemProducaoOuputDTO.fromEntity(op);
    }

    @Transactional
    public OrdemProducaoOuputDTO salvarRetrabalho (OrdemProducaoInputDTO dto){
        OrdemProducao op = OrdemProducao.criarRetrabalho(dto.equipamentoId(), darNomeOP(),dto.capacidadeMaxima());
        
        repository.save(op);
       return OrdemProducaoOuputDTO.fromEntity(op);
    }

    @Transactional
    public OrdemProducaoOuputDTO vincularLote(Long idOP, Long idLote){
      OrdemProducao op = repository.findById(idOP).orElseThrow(() -> new OPNotFoundException("Ordem de produção não encontrada"));
        Lote lote = loteRepository.findById(idLote).orElseThrow(() -> new NotFoundLoteException("Lote não encontrado"));

        op.adicionarLote(lote);

        repository.save(op);
        loteRepository.save(lote);

        return OrdemProducaoOuputDTO.fromEntity(op);
    }
    @Transactional
    public OrdemProducaoOuputDTO mudarEquipamento(Long id, Long equipamentoId) {
        OrdemProducao op = repository.findById(id)
                .orElseThrow(() -> new OPNotFoundException("Ordem de produção não encontrada"));
     if (! equipamentoRepository.existsById(equipamentoId)) {
            throw new NotFoundEquipamentoException("Equipamento não encontrado");
        }

       
        op.setEquipamento(equipamentoId);

        repository.save(op);

        return OrdemProducaoOuputDTO.fromEntity(op);
    }


     


    private String darNomeOP(){
        String  nome;
   
        do{
              String prefixo ="OP";
        int numero = new Random().nextInt(999_999);
         nome = String.format("%s%d", prefixo,numero);
            
        }while(repository.existsByNumeroOP(nome));

        return nome;
    }





   
}
