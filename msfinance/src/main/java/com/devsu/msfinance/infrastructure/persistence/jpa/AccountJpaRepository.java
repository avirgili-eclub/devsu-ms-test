package com.devsu.msfinance.infrastructure.persistence.jpa;

import com.devsu.msfinance.domain.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AccountJpaRepository extends JpaRepository<Account, Long> {

    boolean existsByAccountNumber(String accountNumber);

    List<Account> findByClientId(Long clientId);
}
