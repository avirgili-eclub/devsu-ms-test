package com.devsu.msfinance.domain.port.in;

import com.devsu.msfinance.domain.model.Account;

import java.util.List;

public interface AccountUseCase {

    Account create(Account account);

    Account getById(Long id);

    List<Account> getAll();

    Account update(Long id, Account account);

    void deleteById(Long id);
}
