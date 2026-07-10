package org.example.bookstore.dto;

import java.math.BigDecimal;

/** BookResponseDto - Data Transfer Object for returning book data via REST API. */
public record BookResponseDto(
    /** Unique identifier (primary key) - database-generated. */
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
    Integer publishedYear,

    /** ID of the author associated with this book. */
    Long authorId,

    /** Author name copied into the response while the transaction is open. */
    String authorName
) {}

