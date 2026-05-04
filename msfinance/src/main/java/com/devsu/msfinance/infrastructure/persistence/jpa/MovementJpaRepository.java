package com.devsu.msfinance.infrastructure.persistence.jpa;

import com.devsu.msfinance.domain.model.Movement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface MovementJpaRepository extends JpaRepository<Movement, Long> {

    // Derived query
    List<Movement> findByAccount_Id(Long accountId);

    // @Query explícita para buscar movimientos por clientId y rango de fechas
    @Query("SELECT m FROM Movement m WHERE m.account.clientId = :clientId AND m.date BETWEEN :from AND :to")
    List<Movement> findByClientIdAndDateRange(
            @Param("clientId") Long clientId,
            @Param("from") LocalDateTime from,
            @Param("to") LocalDateTime to);

    void deleteByAccount_Id(Long accountId);
}
