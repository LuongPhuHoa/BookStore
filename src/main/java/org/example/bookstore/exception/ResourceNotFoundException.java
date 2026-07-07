package org.example.bookstore.exception;

/**
 * ResourceNotFoundException - Custom exception for missing resources.
 *
 * ═══════════════════════════════════════════════════════════════════════════════
 * PURPOSE:
 * ═══════════════════════════════════════════════════════════════════════════════
 * Thrown when a requested resource (entity) is not found in the database.
 *
 * EXAMPLES:
 * - GET /api/books/999 (book with ID 999 doesn't exist)
 * - GET /api/authors/555 (author not found)
 * - GET /api/reviews/777 (review not found)
 *
 * ═══════════════════════════════════════════════════════════════════════════════
 * WHY CUSTOM EXCEPTION?
 * ═══════════════════════════════════════════════════════════════════════════════
 * Generic exceptions like IllegalArgumentException don't clearly indicate
 * the reason for failure (could be any invalid argument).
 *
 * ResourceNotFoundException clearly states: "What you're looking for doesn't exist"
 *
 * EXCEPTION HIERARCHY:
 * Throwable
 *   ↓
 * Exception
 *   ↓
 * RuntimeException (unchecked, doesn't require throws keyword)
 *   ↓
 * ResourceNotFoundException (custom)
 *
 * ═══════════════════════════════════════════════════════════════════════════════
 * USAGE IN SERVICE:
 * ═══════════════════════════════════════════════════════════════════════════════
 * public BookResponseDto getBook(Long id) {
 *     return bookRepository.findById(id)
 *         .map(bookMapper::toResponseDto)
 *         .orElseThrow(() -> new ResourceNotFoundException("Book not found: " + id));
 * }
 *
 * ═══════════════════════════════════════════════════════════════════════════════
 * HANDLING IN GlobalExceptionHandler:
 * ═══════════════════════════════════════════════════════════════════════════════
 * @ExceptionHandler(ResourceNotFoundException.class)
 * public ResponseEntity<ErrorResponse> handleResourceNotFound(...) {
 *     // Catches ResourceNotFoundException and returns HTTP 404
 * }
 *
 * HTTP RESPONSE:
 * Status: 404 Not Found
 * Body: { "status": 404, "error": "Not Found", "message": "Book not found: 999", ... }
 *
 * @see GlobalExceptionHandler#handleResourceNotFound
 */
public class ResourceNotFoundException extends RuntimeException {

    /**
     * Constructor with error message.
     *
     * @param message - descriptive message (should include what wasn't found)
     *                  Example: "Book not found: 999" or "Author with ID 555 does not exist"
     */
    public ResourceNotFoundException(String message) {
        super(message);
    }
}
