package com.devsu.msfinance.infrastructure.persistence.jpa;

import com.devsu.msfinance.domain.model.Movement;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface MovementJpaRepository extends JpaRepository<Movement, Long> {

    List<Movement> findByAccount_Id(Long accountId);

    List<Movement> findByAccount_ClientIdAndDateBetween(Long clientId, LocalDateTime from, LocalDateTime to);
}
