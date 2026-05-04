package com.devsu.msidentity.application.service;

import com.devsu.msidentity.domain.exception.ClientNotFoundException;
import com.devsu.msidentity.domain.model.Client;
import com.devsu.msidentity.domain.port.in.ClientUseCase;
import com.devsu.msidentity.domain.port.out.ClientRepository;
import com.devsu.msidentity.infrastructure.messaging.ClientEventPublisher;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ClientService implements ClientUseCase {

    private final ClientRepository clientRepository;
    private final PasswordEncoder passwordEncoder;
    private final ClientEventPublisher eventPublisher;

    @Override
    public Client create(Client client) {
        if (clientRepository.existsByIdentification(client.getIdentification())) {
            throw new IllegalArgumentException(
                    "Identification already registered: " + client.getIdentification());
        }
        client.setClientId(UUID.randomUUID().toString());
        client.setPassword(passwordEncoder.encode(client.getPassword()));
        Client saved = clientRepository.save(client);
        eventPublisher.publishClientCreated(saved.getId());
        return saved;
    }

    @Override
    public Client getById(Long id) {
        return clientRepository.findById(id)
                .orElseThrow(() -> new ClientNotFoundException(id));
    }

    @Override
    public List<Client> getAll() {
        return clientRepository.findAll();
    }

    @Override
    public Client update(Long id, Client client) {
        Client existing = getById(id);
        existing.setName(client.getName());
        existing.setGender(client.getGender());
        existing.setAge(client.getAge());
        existing.setIdentification(client.getIdentification());
        existing.setAddress(client.getAddress());
        existing.setPhone(client.getPhone());
        existing.setStatus(client.isStatus());
        if (client.getPassword() != null && !client.getPassword().isBlank()) {
            existing.setPassword(passwordEncoder.encode(client.getPassword()));
        }
        return clientRepository.save(existing);
    }

    @Override
    public void deleteById(Long id) {
        Client client = getById(id);
        clientRepository.deleteById(id);
        eventPublisher.publishClientDeleted(client.getId());
    }
}
