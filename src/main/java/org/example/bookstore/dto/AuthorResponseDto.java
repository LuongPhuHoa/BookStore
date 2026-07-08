package org.example.bookstore.dto;

/**
 * AuthorResponseDto - compact output DTO for author APIs.
 */
public record AuthorResponseDto(
    Long id,
    String name,
    String nationality
) {}
