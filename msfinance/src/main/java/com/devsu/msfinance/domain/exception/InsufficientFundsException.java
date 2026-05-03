package com.devsu.msfinance.domain.exception;

import java.math.BigDecimal;

public class InsufficientFundsException extends RuntimeException {

    public InsufficientFundsException(BigDecimal currentBalance, BigDecimal amount) {
        super("Insufficient funds: current balance is " + currentBalance + ", requested withdrawal is " + amount);
    }
}
