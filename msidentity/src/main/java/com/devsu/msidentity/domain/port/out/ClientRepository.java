package com.devsu.msidentity.domain.port.out;

import com.devsu.msidentity.domain.model.Client;

import java.util.List;
import java.util.Optional;

public interface ClientRepository {

    Client save(Client client);

    Optional<Client> findById(Long id);

    Optional<Client> findByClientId(String clientId);

    List<Client> findAll();

    void deleteById(Long id);

    boolean existsByIdentification(String identification);
}
