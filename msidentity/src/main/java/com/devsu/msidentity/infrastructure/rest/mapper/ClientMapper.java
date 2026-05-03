package com.devsu.msidentity.infrastructure.rest.mapper;

import com.devsu.msidentity.domain.model.Client;
import com.devsu.msidentity.infrastructure.rest.dto.ClientRequest;
import com.devsu.msidentity.infrastructure.rest.dto.ClientResponse;
import org.springframework.stereotype.Component;

@Component
public class ClientMapper {

    public Client toDomain(ClientRequest request) {
        return Client.builder()
                .name(request.getName())
                .gender(request.getGender())
                .age(request.getAge())
                .identification(request.getIdentification())
                .address(request.getAddress())
                .phone(request.getPhone())
                .password(request.getPassword())
                .status(request.getStatus())
                .build();
    }

    public ClientResponse toResponse(Client client) {
        return ClientResponse.builder()
                .id(client.getId())
                .clientId(client.getClientId())
                .name(client.getName())
                .gender(client.getGender())
                .age(client.getAge())
                .identification(client.getIdentification())
                .address(client.getAddress())
                .phone(client.getPhone())
                .status(client.isStatus())
                .build();
    }
}
