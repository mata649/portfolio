package com.mata649.portfolio.shared.exceptions;

public class EntityConflictException extends RuntimeException {
    public EntityConflictException(String message) {
        super(message);
    }
}
