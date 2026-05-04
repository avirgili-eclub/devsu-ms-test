package com.devsu.msfinance.domain.exception;

public class UnknownClientException extends RuntimeException {

    public UnknownClientException(Long clientId) {
        super("Client not found in msfinance registry: " + clientId);
    }
}
