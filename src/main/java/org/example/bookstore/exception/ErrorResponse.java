package org.example.bookstore.exception;

import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * ErrorResponse - Standard JSON error response for REST API failures.
 *
 * ═══════════════════════════════════════════════════════════════════════════════
 * PURPOSE:
 * ═══════════════════════════════════════════════════════════════════════════════
 * Provides a consistent format for all error responses sent to REST API clients.
 * Instead of leaking raw exceptions, we return professional error information.
 *
 * ═══════════════════════════════════════════════════════════════════════════════
 * JSON RESPONSE EXAMPLE:
 * ═══════════════════════════════════════════════════════════════════════════════
 * HTTP 404 Not Found:
 * {
 *   "timestamp": "2024-11-15T10:30:45.123Z",
 *   "status": 404,
 *   "error": "Not Found",
 *   "message": "Book not found: 999",
 *   "path": "/api/books/999",
 *   "validationErrors": {}
 * }
 *
 * HTTP 400 Bad Request (with validation errors):
 * {
 *   "timestamp": "2024-11-15T10:30:50.456Z",
 *   "status": 400,
 *   "error": "Bad Request",
 *   "message": "Validation failed",
 *   "path": "/api/books",
 *   "validationErrors": {
 *     "title": "must not be blank",
 *     "price": "must be greater than 0"
 *   }
 * }
 *
 * ═══════════════════════════════════════════════════════════════════════════════
 * FIELDS EXPLAINED:
 * ═══════════════════════════════════════════════════════════════════════════════
 * timestamp:
 *   - When the error occurred
 *   - Type: Instant (ISO-8601 format: 2024-11-15T10:30:45.123Z)
 *   - Useful for client-side logging, debugging server timestamps
 *
 * status:
 *   - HTTP status code
 *   - Examples: 404 (Not Found), 400 (Bad Request), 500 (Internal Server Error)
 *   - Clients can switch on this to handle different error types
 *
 * error:
 *   - HTTP reason phrase
 *   - Examples: "Not Found", "Bad Request", "Internal Server Error"
 *   - Human-readable version of status code
 *
 * message:
 *   - Descriptive error message
 *   - Examples: "Book not found: 999", "Validation failed"
 *   - What went wrong (but not stack trace for security)
 *
 * path:
 *   - URL path that caused the error
 *   - Examples: "/api/books/999", "/api/books"
 *   - Helps clients debug which endpoint failed
 *
 * validationErrors:
 *   - Field-level validation errors (only for 400 Bad Request)
 *   - Map of field name → error message
 *   - Examples: { "title": "must not be blank", "price": "must be greater than 0" }
 *   - Empty map {} for non-validation errors
 *
 * ═══════════════════════════════════════════════════════════════════════════════
 * HOW IT'S USED:
 * ═══════════════════════════════════════════════════════════════════════════════
 * 1. Exception thrown in controller/service
 * 2. GlobalExceptionHandler catches it
 * 3. Handler creates ErrorResponse(timestamp, status, error, message, path)
 * 4. Spring serializes ErrorResponse to JSON
 * 5. Sent to client in HTTP response body
 *
 * @see org.example.bookstore.exception.GlobalExceptionHandler
 */
@Setter
@Getter
public class ErrorResponse {

    /**
     * When the error occurred (server time).
     * Type: Instant (ISO-8601: 2024-11-15T10:30:45.123Z)
     * Used for debugging and correlating server-side logs
     */
    private Instant timestamp;

    /**
     * HTTP status code.
     * Examples:
     * - 400: Bad Request (client error)
     * - 404: Not Found (resource doesn't exist)
     * - 500: Internal Server Error (server error)
     */
    private int status;

    /**
     * HTTP reason phrase (human-readable status).
     * Examples:
     * - "Bad Request" for status 400
     * - "Not Found" for status 404
     * - "Internal Server Error" for status 500
     */
    private String error;

    /**
     * Descriptive error message (NOT stack trace).
     * Examples:
     * - "Book not found: 999"
     * - "Validation failed"
     * - "An unexpected error occurred"
     * Security: Doesn't leak internal implementation details
     */
    private String message;

    /**
     * Request URL path that caused the error.
     * Examples:
     * - "/api/books/999"
     * - "/api/books"
     * Helps clients debug which endpoint failed
     */
    private String path;

    /**
     * Field-level validation errors.
     * Only populated for HTTP 400 Bad Request (validation failures)
     * Empty map {} for other error types
     * Format: { fieldName: errorMessage }
     * Example: { "title": "must not be blank", "price": "must be greater than 0" }
     */
    private Map<String, String> validationErrors = new LinkedHashMap<>();

    /**
     * Default constructor (required by Jackson for JSON deserialization).
     */
    public ErrorResponse() {
    }

    /**
     * Constructor with all fields (used by GlobalExceptionHandler).
     *
     * @param timestamp - when error occurred
     * @param status - HTTP status code
     * @param error - HTTP reason phrase
     * @param message - error message
     * @param path - request path
     */
    public ErrorResponse(Instant timestamp, int status, String error, String message, String path) {
        this.timestamp = timestamp;
        this.status = status;
        this.error = error;
        this.message = message;
        this.path = path;
    }
}
