package org.example.bookstore.exception;

import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.Map;

/** ErrorResponse - Standard JSON error response for REST API failures. */
@Setter
@Getter
public class ErrorResponse {

    /**
     * When the error occurred (server time).
     * Type: Instant (ISO-8601: 2024-11-15T10:30:45.123Z)
     * Used for debugging and correlating server-side logs
     */
    private Instant timestamp;

    /** HTTP status code. */
    private int status;

    /** HTTP reason phrase (human-readable status). */
    private String error;

    /** Descriptive error message (NOT stack trace). */
    private String message;

    /** Request URL path that caused the error. */
    private String path;

    /** Field-level validation errors. */
    private Map<String, String> validationErrors = new LinkedHashMap<>();

    /**
     * Default constructor (required by Jackson for JSON deserialization).
     */
    public ErrorResponse() {
    }

    /** Constructor with all fields (used by GlobalExceptionHandler). */
    public ErrorResponse(Instant timestamp, int status, String error, String message, String path) {
        this.timestamp = timestamp;
        this.status = status;
        this.error = error;
        this.message = message;
        this.path = path;
    }
}

