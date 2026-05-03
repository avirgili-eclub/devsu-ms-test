package com.devsu.msidentity.domain.port.in;

import com.devsu.msidentity.domain.model.Client;

import java.util.List;

public interface ClientUseCase {

    Client create(Client client);

    Client getById(Long id);

    List<Client> getAll();

    Client update(Long id, Client client);

    void deleteById(Long id);
}
