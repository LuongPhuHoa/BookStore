package org.example.bookstore.dto;

import java.math.BigDecimal;

/**
 * BookResponseDto - Data Transfer Object for returning book data via REST API.
 *
 * ═══════════════════════════════════════════════════════════════════════════════
 * PURPOSE:
 * ═══════════════════════════════════════════════════════════════════════════════
 * This DTO is used to serialize Book entities to JSON for HTTP responses.
 * It includes the generated ID (which is present after database insertion).
 *
 * COMPARISON: BookRequestDto vs BookResponseDto
 * ═══════════════════════════════════════════════════════════════════════════════
 * BookRequestDto (INPUT):
 * - No ID field (client doesn't provide it)
 * - Used for POST /api/books (create)
 * - Used for PUT /api/books/{id} (update)
 *
 * BookResponseDto (OUTPUT):
 * - HAS ID field (database-generated)
 * - Used for responses after save/fetch
 * - Client receives this with the generated ID
 *
 * ═══════════════════════════════════════════════════════════════════════════════
 * DATA FLOW:
 * ═══════════════════════════════════════════════════════════════════════════════
 * 1. Client sends: BookRequestDto (no ID)
 * 2. Service saves: Book entity to database → ID auto-generated
 * 3. Service converts: Book entity → BookResponseDto (with ID)
 * 4. Response sent: HTTP 201 Created + BookResponseDto (with ID)
 * 5. Client receives: { id: 1, title: "...", isbn: "...", price: 45.99, publishedYear: 2018 }
 *
 * ═══════════════════════════════════════════════════════════════════════════════
 * JAVA RECORD ADVANTAGES:
 * ═══════════════════════════════════════════════════════════════════════════════
 * Auto-generated (no need to write manually):
 * - Constructor: BookResponseDto(1, "title", "isbn", 45.99, 2018)
 * - Getters: id(), title(), isbn(), price(), publishedYear()
 * - equals(): Compares all fields
 * - hashCode(): Based on all fields
 * - toString(): BookResponseDto[id=1, title=..., isbn=..., price=..., publishedYear=...]
 *
 * Jackson Integration (automatic JSON serialization):
 * When Spring converts this to JSON:
 * BookResponseDto dto = new BookResponseDto(1, "Effective Java", "978-0134685991", 45.99, 2018);
 * Jackson automatically generates:
 * {
 *   "id": 1,
 *   "title": "Effective Java",
 *   "isbn": "978-0134685991",
 *   "price": 45.99,
 *   "publishedYear": 2018
 * }
 *
 * ═══════════════════════════════════════════════════════════════════════════════
 * EXAMPLE HTTP FLOW:
 * ═══════════════════════════════════════════════════════════════════════════════
 * REQUEST:
 * POST /api/books
 * Content-Type: application/json
 * {
 *   "title": "Effective Java",
 *   "isbn": "978-0134685991",
 *   "price": 45.99,
 *   "publishedYear": 2018
 * }
 *
 * RESPONSE:
 * HTTP 201 Created
 * Content-Type: application/json
 * {
 *   "id": 1,
 *   "title": "Effective Java",
 *   "isbn": "978-0134685991",
 *   "price": 45.99,
 *   "publishedYear": 2018
 * }
 *
 * Notice the "id": 1 in response (database auto-generated)
 *
 * ═══════════════════════════════════════════════════════════════════════════════
 * SEPARATION OF CONCERNS:
 * ═══════════════════════════════════════════════════════════════════════════════
 * Why two DTOs instead of one?
 *
 * ✅ Benefits of separate DTOs:
 * - Input DTO doesn't need ID (invalid to send ID when creating)
 * - Output DTO includes ID (important for client to know what was created)
 * - Can have different validation rules (future commits)
 * - API contract is explicit about required vs optional fields
 * - Future flexibility: Add audit fields (createdAt, updatedAt) to ResponseDto only
 *
 * ❌ If we used one DTO for both:
 * - ID would be optional: Integer id (null on create, set on response)
 * - Client might incorrectly send an ID
 * - Confusing API contract
 *
 * @see org.example.bookstore.entity.Book
 * @see org.example.bookstore.dto.BookRequestDto
 * @see org.example.bookstore.mapper.BookMapper
 */
public record BookResponseDto(
    /**
     * Unique identifier (primary key) - database-generated.
     *
     * WHEN IS IT SET?
     * - NULL before saving to database
     * - Set by PostgreSQL during INSERT (IDENTITY strategy)
     * - Returned in response so client knows the created resource ID
     *
     * USAGE:
     * Long createdBookId = response.id();  // e.g., 1
     * GET /api/books/1  ← Use this ID to fetch
     *
     * WHY NOT set by client?
     * - Could violate PRIMARY KEY constraint (duplicate IDs)
     * - Database auto-increment ensures unique IDs
     * - RESTful convention: Server generates IDs
     */
    Long id,

    /**
     * Book title - same as in request.
     * Copied from Book entity.title
     * Example: "Effective Java"
     */
    String title,

    /**
     * ISBN (International Standard Book Number) - same as in request.
     * Copied from Book entity.isbn
     * Unique identifier for the book
     * Example: "978-0134685991"
     */
    String isbn,

    /**
     * Book price - same as in request.
     * Copied from Book entity.price
     * Monetary value in dollars
     * Example: 45.99
     */
    BigDecimal price,

    /**
     * Publication year - same as in request.
     * Copied from Book entity.publishedYear
     * Can be null if not provided
     * Example: 2018, or null
     */
    Integer publishedYear
) {}
