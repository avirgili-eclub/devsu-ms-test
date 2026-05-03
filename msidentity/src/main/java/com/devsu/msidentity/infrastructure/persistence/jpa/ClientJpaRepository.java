package com.devsu.msidentity.infrastructure.persistence.jpa;

import com.devsu.msidentity.domain.model.Client;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ClientJpaRepository extends JpaRepository<Client, Long> {

    Optional<Client> findByClientId(String clientId);

    boolean existsByIdentification(String identification);
}
