package com.example.backend.exception;

public class EntityNotFoundException extends RuntimeException {
    public EntityNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public EntityNotFoundException(String message) {
        super(message);
    }
}
