package com.devsu.msidentity.infrastructure.persistence;

import com.devsu.msidentity.domain.model.Client;
import com.devsu.msidentity.domain.port.out.ClientRepository;
import com.devsu.msidentity.infrastructure.persistence.jpa.ClientJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class ClientRepositoryAdapter implements ClientRepository {

    private final ClientJpaRepository jpa;

    @Override
    public Client save(Client client) {
        return jpa.save(client);
    }

    @Override
    public Optional<Client> findById(Long id) {
        return jpa.findById(id);
    }

    @Override
    public Optional<Client> findByClientId(String clientId) {
        return jpa.findByClientId(clientId);
    }

    @Override
    public List<Client> findAll() {
        return jpa.findAll();
    }

    @Override
    public void deleteById(Long id) {
        jpa.deleteById(id);
    }

    @Override
    public boolean existsByIdentification(String identification) {
        return jpa.existsByIdentification(identification);
    }
}
