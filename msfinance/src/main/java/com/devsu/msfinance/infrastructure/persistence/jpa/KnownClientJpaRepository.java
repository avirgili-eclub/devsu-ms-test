package com.devsu.msfinance.infrastructure.persistence.jpa;

import com.devsu.msfinance.domain.model.KnownClient;
import org.springframework.data.jpa.repository.JpaRepository;

public interface KnownClientJpaRepository extends JpaRepository<KnownClient, Long> {
}
