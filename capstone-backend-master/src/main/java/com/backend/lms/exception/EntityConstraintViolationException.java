package com.backend.lms.exception;

public class EntityConstraintViolationException extends RuntimeException {
    public EntityConstraintViolationException(String message) {
        super(message);
    }
}
