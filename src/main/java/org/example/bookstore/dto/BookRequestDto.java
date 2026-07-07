package org.example.bookstore.dto;

import java.math.BigDecimal;

/**
 * BookRequestDto - Data Transfer Object for creating/updating books via REST API.
 *
 * ═══════════════════════════════════════════════════════════════════════════════
 * WHY USE DTOS? (Separation of Concerns)
 * ═══════════════════════════════════════════════════════════════════════════════
 * 1. DECOUPLING: API contract is separate from database schema
 *    - You can change internal Book entity without breaking clients
 *    - Example: Add internal audit fields (createdBy, createdAt) without exposing them
 *
 * 2. SECURITY: DTOs only expose what clients should see
 *    - Never return entity directly with sensitive fields
 *    - Example: Don't expose password, internal IDs, or system notes
 *    - DTO: Only needed fields for the client
 *
 * 3. VALIDATION: DTOs have @NotBlank, @Min, @Max constraints
 *    - Validates input at API boundary
 *    - Prevents invalid data reaching service/database
 *    - Example: @Min(0) BigDecimal price ensures price >= 0
 *
 * 4. MAINTAINABILITY: Clean separation of layers
 *    - Controller layer only knows DTOs
 *    - Controller doesn't know about Entity or business logic
 *    - Service layer converts DTO → Entity
 *
 * ═══════════════════════════════════════════════════════════════════════════════
 * WHY USE JAVA RECORDS? (Java 14+)
 * ═══════════════════════════════════════════════════════════════════════════════
 * Records are perfect for immutable value objects like DTOs:
 *
 * ✅ BENEFITS:
 * - Immutable by default (no setters)
 * - Auto-generates: constructor, getters, equals(), hashCode(), toString()
 * - Compact syntax (less boilerplate than class)
 * - Serializable by default (Jackson handles them perfectly)
 *
 * ❌ If we used a class instead:
 * Would need:
 * - @Getter @Setter (or write getters/setters manually)
 * - NoArgsConstructor for JSON deserialization
 * - AllArgsConstructor for creation
 * - Lots of boilerplate code
 *
 * RECORD METHODS (auto-generated):
 * - title() → getter method
 * - isbn() → getter method
 * - price() → getter method
 * - publishedYear() → getter method
 *
 * ═══════════════════════════════════════════════════════════════════════════════
 * DATA FLOW IN REST API:
 * ═══════════════════════════════════════════════════════════════════════════════
 * 1. CLIENT sends JSON:
 *    POST /api/books
 *    {
 *      "title": "Effective Java",
 *      "isbn": "978-0134685991",
 *      "price": 45.99,
 *      "publishedYear": 2018
 *    }
 *
 * 2. SPRING CONTROLLER (@RequestBody):
 *    Jackson deserializes JSON → BookRequestDto instance
 *    @PostMapping
 *    public ResponseEntity<BookResponseDto> createBook(
 *        @RequestBody BookRequestDto requestDto  ← Deserialized here
 *    )
 *
 * 3. SPRING SERVICE (Mapper):
 *    BookRequestDto → Book entity
 *    Book book = bookMapper.toEntity(requestDto);
 *    ✓ MapStruct auto-matches fields with same names
 *    ✓ If field names differ, use @Mapping
 *
 * 4. DATABASE PERSISTENCE:
 *    @Transactional → repository.save(book)
 *    → Hibernate INSERT SQL generated
 *    → Book saved to database (ID auto-generated)
 *
 * 5. RESPONSE DTO:
 *    Book entity → BookResponseDto
 *    BookResponseDto response = bookMapper.toResponseDto(savedBook);
 *    Jackson serializes BookResponseDto → JSON
 *    → Send to client with generated ID
 *
 * ═══════════════════════════════════════════════════════════════════════════════
 * VALIDATION CONSTRAINTS (not yet added, but future commits will add):
 * ═══════════════════════════════════════════════════════════════════════════════
 * Future versions may add Jakarta Validation constraints:
 *
 * public record BookRequestDto(
 *     @NotBlank(message = "Title cannot be blank")
 *     String title,
 *
 *     @NotBlank(message = "ISBN cannot be blank")
 *     @Pattern(regexp = "ISBN.*") // Only accept ISBN format
 *     String isbn,
 *
 *     @NotNull
 *     @Positive(message = "Price must be greater than 0")
 *     BigDecimal price,
 *
 *     @Positive(message = "Published year must be greater than 0")
 *     Integer publishedYear
 * ) {}
 *
 * These constraints are:
 * - Validated at API boundary (@Valid in controller)
 * - Spring returns 400 Bad Request if validation fails
 * - Error response includes all validation messages
 *
 * @see org.example.bookstore.entity.Book
 * @see org.example.bookstore.dto.BookResponseDto
 * @see org.example.bookstore.mapper.BookMapper
 */
public record BookRequestDto(
    /**
     * Book title - required field.
     * Example: "Effective Java", "Clean Code", "Design Patterns"
     */
    String title,

    /**
     * International Standard Book Number - unique identifier.
     * Format: Usually 13 digits (ISBN-13) or 10 digits (ISBN-10)
     * Example: "978-0134685991"
     * Database constraint: UNIQUE (no duplicates allowed)
     */
    String isbn,

    /**
     * Book price - monetary value in dollars.
     * Type: BigDecimal (preserves precision, better than double)
     * Example: BigDecimal.valueOf(45.99)
     * Constraint: Must be >= 0 (typically positive)
     */
    BigDecimal price,

    /**
     * Year the book was published - optional field (can be null).
     * Example: 2018, 2005, null
     * Typical range: 1900-2100
     * Nullable: Yes (if not provided, defaults to null)
     */
    Integer publishedYear
) {}
