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

/**
 * GlobalExceptionHandler - Centralized exception handling for REST API.
 *
 * ═══════════════════════════════════════════════════════════════════════════════
 * PURPOSE:
 * ═══════════════════════════════════════════════════════════════════════════════
 * Provides consistent, professional error responses across the entire API.
 *
 * WITHOUT GlobalExceptionHandler:
 * - Each controller must manually catch exceptions
 * - Response format would be inconsistent
 * - Errors would leak stack traces to clients
 * - Hard to maintain
 *
 * WITH GlobalExceptionHandler:
 * - Exception handling is centralized
 * - All errors have consistent JSON format
 * - Response includes timestamp, status, message, path
 * - Can log exceptions for debugging
 * - Easy to add new exception types
 *
 * ═══════════════════════════════════════════════════════════════════════════════
 * @RestControllerAdvice:
 * ═══════════════════════════════════════════════════════════════════════════════
 * @RestControllerAdvice = @ControllerAdvice + @ResponseBody
 *
 * @ControllerAdvice:
 * - Spring discovers this class automatically
 * - Applies to ALL controllers (global)
 * - Methods intercept exceptions thrown by any controller
 *
 * @ResponseBody:
 * - Return values are serialized to JSON
 * - Not view names (no JSP lookup)
 *
 * ═══════════════════════════════════════════════════════════════════════════════
 * HOW IT WORKS:
 * ═══════════════════════════════════════════════════════════════════════════════
 * 1. Controller method throws exception
 * 2. Spring catches exception
 * 3. Searches @RestControllerAdvice classes for @ExceptionHandler methods
 * 4. Finds matching handler (by exception type)
 * 5. Calls handler method
 * 6. Returns ErrorResponse (serialized to JSON)
 * 7. Client receives HTTP error with JSON body
 *
 * EXAMPLE FLOW:
 *
 * Client sends: GET /api/books/999 (book doesn't exist)
 *   ↓
 * BookService.getBook(999) throws IllegalArgumentException("Book not found: 999")
 *   ↓
 * Spring catches IllegalArgumentException
 *   ↓
 * Searches for @ExceptionHandler(IllegalArgumentException.class)
 *   ↓
 * handleUnexpected() executed
 *   ↓
 * Returns ErrorResponse(404, "Not Found", "Book not found: 999", ...)
 *   ↓
 * Spring serializes to JSON
 *   ↓
 * HTTP 404 response sent to client
 *
 * ═══════════════════════════════════════════════════════════════════════════════
 * @ExceptionHandler EXPLANATION:
 * ═══════════════════════════════════════════════════════════════════════════════
 * @ExceptionHandler(Exception.class)
 * - Tells Spring this method handles Exception (and all subclasses)
 * - When exception of this type is thrown → call this method
 *
 * METHOD PARAMETERS:
 * - Exception exception: The thrown exception (inspect for details)
 * - HttpServletRequest request: The HTTP request that caused error
 *
 * RETURN TYPE:
 * - ResponseEntity<ErrorResponse>: Allows setting HTTP status + body
 *
 * @see org.springframework.web.bind.annotation.RestControllerAdvice
 * @see org.springframework.web.bind.annotation.ExceptionHandler
 */
@RestControllerAdvice  // Global exception handler for all @RestController classes
public class GlobalExceptionHandler {

    /**
     * Handle ResourceNotFoundException (custom exception).
     *
     * WHEN IS THIS CALLED?
     * When any @Controller throws ResourceNotFoundException
     * Example:
     * throw new ResourceNotFoundException("Book not found");
     *
     * HTTP RESPONSE:
     * Status: 404 Not Found
     * Body: { "status": 404, "error": "Not Found", "message": "Book not found", ... }
     *
     * WHY 404?
     * HTTP 404 = "Resource not found"
     * Perfect for business logic errors where entity doesn't exist
     *
     * @param exception - the thrown exception
     * @param request - the HTTP request
     * @return ErrorResponse with 404 status
     */
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

    /**
     * Handle MethodArgumentNotValidException (validation errors).
     *
     * WHEN IS THIS CALLED?
     * When @RequestBody validation fails
     * Example:
     * {
     *   "title": "",     // @NotBlank violated
     *   "price": -10     // @Positive violated
     * }
     *
     * HTTP RESPONSE:
     * Status: 400 Bad Request
     * Body: {
     *   "status": 400,
     *   "error": "Bad Request",
     *   "message": "Validation failed",
     *   "validationErrors": {
     *     "title": "must not be blank",
     *     "price": "must be greater than 0"
     *   },
     *   ...
     * }
     *
     * WHY 400?
     * HTTP 400 = "Bad Request"
     * Client sent invalid data (incorrect format, missing required fields, etc.)
     *
     * FUTURE COMMITS:
     * Will add @NotBlank, @Positive, etc. to DTOs
     * Then this handler will catch those violations
     *
     * @param exception - MethodArgumentNotValidException (contains binding errors)
     * @param request - the HTTP request
     * @return ErrorResponse with 400 status + field errors
     */
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

    /**
     * Handle unexpected exceptions (catch-all).
     *
     * WHEN IS THIS CALLED?
     * When ANY other exception is thrown (not handled by specific handlers above)
     * Examples:
     * - NullPointerException
     * - IllegalArgumentException (if not caught earlier)
     * - IllegalStateException
     * - Any unknown exception
     *
     * HTTP RESPONSE:
     * Status: 500 Internal Server Error
     * Body: { "status": 500, "error": "Internal Server Error", "message": "An unexpected error occurred", ... }
     *
     * WHY 500?
     * HTTP 500 = "Internal Server Error"
     * Something went wrong on the server (not client's fault)
     *
     * SECURITY NOTE:
     * Message is generic ("An unexpected error occurred"), not the actual error
     * This prevents leaking internal stack traces to untrusted clients
     * (server.error.include-message=never in application.properties)
     * Actual error is logged server-side for debugging
     *
     * @param exception - any exception not handled above
     * @param request - the HTTP request
     * @return ErrorResponse with 500 status
     */
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

    /**
     * Helper method to build ErrorResponse object.
     *
     * @param status - HTTP status code
     * @param message - error message
     * @param path - request URL path
     * @return ErrorResponse object
     */
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
