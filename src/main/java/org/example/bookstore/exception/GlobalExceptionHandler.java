package org.example.bookstore.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.Map;

/** GlobalExceptionHandler - Centralized exception handling for REST API. */
@RestControllerAdvice  // Global exception handler for all @RestController classes
public class GlobalExceptionHandler {

    /** Handle ResourceNotFoundException (custom exception). */
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleResourceNotFound(
            ResourceNotFoundException exception,
            HttpServletRequest request
    ) {
        // Build error response with 404 status
        ErrorResponse response = buildError(
            HttpStatus.NOT_FOUND,
            exception.getMessage(),
            request.getRequestURI()
        );
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    /** Handle MethodArgumentNotValidException (validation errors). */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidation(
            MethodArgumentNotValidException exception,
            HttpServletRequest request
    ) {
        // Build error response with 400 status
        ErrorResponse response = buildError(
            HttpStatus.BAD_REQUEST,
            "Validation failed",
            request.getRequestURI()
        );
        
        // Extract field-level validation errors
        Map<String, String> fieldErrors = new LinkedHashMap<>();
        exception.getBindingResult().getFieldErrors()
                .forEach(error -> fieldErrors.put(
                    error.getField(),
                    error.getDefaultMessage()
                ));
        response.setValidationErrors(fieldErrors);
        
        return ResponseEntity.badRequest().body(response);
    }

    /** Handle unexpected exceptions (catch-all). */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleUnexpected(
            Exception exception,
            HttpServletRequest request
    ) {
        // Log the exception for debugging (not shown to client)
        exception.printStackTrace();
        
        // Build error response with 500 status (generic message for security)
        ErrorResponse response = buildError(
            HttpStatus.INTERNAL_SERVER_ERROR,
            "An unexpected error occurred",
            request.getRequestURI()
        );
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }

    /** Helper method to build ErrorResponse object. */
    private ErrorResponse buildError(HttpStatus status, String message, String path) {
        return new ErrorResponse(
            Instant.now(),              // Current timestamp
            status.value(),             // HTTP status code (404, 400, 500, etc.)
            status.getReasonPhrase(),   // HTTP reason ("Not Found", "Bad Request", etc.)
            message,                    // Custom error message
            path                        // Request path that caused error
        );
    }
}

