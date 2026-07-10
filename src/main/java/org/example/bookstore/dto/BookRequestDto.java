package org.example.bookstore.dto;

import java.math.BigDecimal;

/** BookRequestDto - Data Transfer Object for creating/updating books via REST API. */
public record BookRequestDto(
    /*
      Book title - required field.
      Example: "Effective Java", "Clean Code", "Design Patterns"
     */
    String title,

    /*
      International Standard Book Number - unique identifier.
      Format: Usually 13 digits (ISBN-13) or 10 digits (ISBN-10)
      Example: "978-0134685991"
      Database constraint: UNIQUE (no duplicates allowed)
     */
    String isbn,

    /*
      Book price - monetary value in dollars.
      Type: BigDecimal (preserves precision, better than double)
      Example: BigDecimal.valueOf(45.99)
      Constraint: Must be >= 0 (typically positive)
     */
    BigDecimal price,

    /*
      Year the book was published - optional field (can be null).
      Example: 2018, 2005, null
      Typical range: 1900-2100
      Nullable: Yes (if not provided, defaults to null)
     */
    Integer publishedYear,

    /*
      Existing author ID. Commit 04 uses this to attach each Book to one Author.
     */
    Long authorId
) {}

