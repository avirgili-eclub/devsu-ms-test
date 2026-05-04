package com.devsu.msidentity.application.service;

import com.devsu.msidentity.domain.exception.ClientNotFoundException;
import com.devsu.msidentity.domain.model.Client;
import com.devsu.msidentity.domain.port.out.ClientRepository;
import com.devsu.msidentity.infrastructure.messaging.ClientEventPublisher;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ClientServiceTest {

    @Mock
    private ClientRepository clientRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private ClientEventPublisher eventPublisher;

    @InjectMocks
    private ClientService clientService;

    @Test
    void create_shouldGenerateClientIdAndEncodePassword() {
        Client input = clientWithPassword("plain-password");
        when(clientRepository.existsByIdentification(input.getIdentification())).thenReturn(false);
        when(passwordEncoder.encode("plain-password")).thenReturn("hashed-password");
        when(clientRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        Client result = clientService.create(input);

        assertThat(result.getClientId()).isNotBlank();
        assertThat(result.getPassword()).isEqualTo("hashed-password");
        verify(clientRepository).save(input);
    }

    @Test
    void create_whenIdentificationAlreadyExists_shouldThrowIllegalArgumentException() {
        Client input = clientWithPassword("any");
        when(clientRepository.existsByIdentification(input.getIdentification())).thenReturn(true);

        assertThatThrownBy(() -> clientService.create(input))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining(input.getIdentification());

        verify(clientRepository, never()).save(any());
    }

    @Test
    void getById_whenClientDoesNotExist_shouldThrowClientNotFoundException() {
        when(clientRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> clientService.getById(99L))
                .isInstanceOf(ClientNotFoundException.class);
    }

    @Test
    void update_whenPasswordIsBlank_shouldNotReEncodePassword() {
        Client existing = clientWithPassword("existing-hash");
        existing.setId(1L);
        Client update = clientWithPassword("");

        when(clientRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(clientRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        clientService.update(1L, update);

        verify(passwordEncoder, never()).encode(anyString());
        assertThat(existing.getPassword()).isEqualTo("existing-hash");
    }

    @Test
    void deleteById_shouldDeleteClientAndPublishEvent() {
        Client client = clientWithPassword("any");
        client.setId(5L);
        when(clientRepository.findById(5L)).thenReturn(Optional.of(client));

        clientService.deleteById(5L);

        verify(clientRepository).deleteById(5L);
        verify(eventPublisher).publishClientDeleted(5L);
    }

    private Client clientWithPassword(String password) {
        Client client = new Client();
        client.setId(1L);
        client.setName("Test User");
        client.setIdentification("1234567890");
        client.setPassword(password);
        client.setStatus(true);
        return client;
    }
}
