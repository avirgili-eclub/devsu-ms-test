package com.devsu.msfinance.infrastructure.persistence.jpa;

import com.devsu.msfinance.domain.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountJpaRepository extends JpaRepository<Account, Long> {

    boolean existsByAccountNumber(String accountNumber);
}
