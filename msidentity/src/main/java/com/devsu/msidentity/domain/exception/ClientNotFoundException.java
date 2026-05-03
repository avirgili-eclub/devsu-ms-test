package com.devsu.msidentity.domain.exception;

public class ClientNotFoundException extends RuntimeException {

    public ClientNotFoundException(Long id) {
        super("Client not found with id: " + id);
    }
}
