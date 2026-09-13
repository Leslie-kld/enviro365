package com.enviro.assessment.junior.leslie.exception;

import com.enviro.assessment.junior.leslie.dto.ErrorResponseDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

/**
 * Centralised exception handling so every controller returns the same
 * structured error shape instead of leaking stack traces or default
 * Spring error pages.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponseDTO> handleNotFound(ResourceNotFoundException ex) {
        return buildResponse(HttpStatus.NOT_FOUND, ex.getMessage());
    }

    @ExceptionHandler(WithdrawalValidationException.class)
    public ResponseEntity<ErrorResponseDTO> handleValidation(WithdrawalValidationException ex) {
        return buildResponse(HttpStatus.BAD_REQUEST, ex.getMessage());
    }

    // Triggered when @Valid fails on a request body (Jakarta Validation annotations on DTOs)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponseDTO> handleBeanValidation(MethodArgumentNotValidException ex) {
        String message = ex.getBindingResult().getFieldErrors().stream()
                .map(FieldError::getDefaultMessage)
                .collect(Collectors.joining("; "));
        return buildResponse(HttpStatus.BAD_REQUEST, message);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponseDTO> handleIllegalArgument(IllegalArgumentException ex) {
        return buildResponse(HttpStatus.BAD_REQUEST, ex.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponseDTO> handleGeneric(Exception ex) {
        return buildResponse(HttpStatus.INTERNAL_SERVER_ERROR, "An unexpected error occurred: " + ex.getMessage());
    }

    private ResponseEntity<ErrorResponseDTO> buildResponse(HttpStatus status, String message) {
        ErrorResponseDTO body = new ErrorResponseDTO(
                LocalDateTime.now(),
                status.value(),
                status.getReasonPhrase(),
                message
        );
        return ResponseEntity.status(status).body(body);
    }
}
