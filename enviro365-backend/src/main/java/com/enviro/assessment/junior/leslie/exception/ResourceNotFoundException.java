package com.enviro.assessment.junior.leslie.exception;

/**
 * Thrown when a requested entity (investor, portfolio, withdrawal, etc.)
 * cannot be found. Mapped to HTTP 404 by the GlobalExceptionHandler.
 */
public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException(String message) {
        super(message);
    }
}
