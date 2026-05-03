package com.devsu.msfinance.domain.exception;

public class AccountNotFoundException extends RuntimeException {

    public AccountNotFoundException(Long id) {
        super("Account not found with id: " + id);
    }
}
