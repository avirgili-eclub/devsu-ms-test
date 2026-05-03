package com.devsu.msfinance.domain.exception;

public class MovementNotFoundException extends RuntimeException {

    public MovementNotFoundException(Long id) {
        super("Movement not found with id: " + id);
    }
}
