package com.example.mes.cliente.application.mapper;

import com.example.mes.cliente.application.DTO.ClienteResponseDTO;
import com.example.mes.cliente.application.DTO.ClienteRquestDTO;
import com.example.mes.cliente.domain.Cliente;
import org.springframework.stereotype.Component;

@Component
public class ClienteMapper {


    public Cliente toEntity(ClienteRquestDTO clienteRquestDTO) {
        Cliente cliente = new Cliente();
        cliente.setNome(clienteRquestDTO.nome());
        cliente.setEmpresa(clienteRquestDTO.empresa());
        cliente.setCpnj(clienteRquestDTO.cpnj());
        cliente.setEmail(clienteRquestDTO.email());
        return cliente;


    }


    public ClienteResponseDTO toDTO(Cliente cliente) {
        return new ClienteResponseDTO(cliente.getNome(), cliente.getEmpresa(), cliente.getCpnj(), cliente.getEmail());
    }
}

