package com.enviro.assessment.junior.leslie.exception;

/**
 * Thrown when a withdrawal request violates one of Enviro365's business
 * rules (age restriction, balance cap, 90% cap, non-positive amount).
 * Mapped to HTTP 400 by the GlobalExceptionHandler.
 */
public class WithdrawalValidationException extends RuntimeException {

    public WithdrawalValidationException(String message) {
        super(message);
    }
}
