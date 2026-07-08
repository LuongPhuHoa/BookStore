package org.example.bookstore.dto;

/**
 * AuthorRequestDto - compact input DTO for author APIs.
 */
public record AuthorRequestDto(
    String name,
    String nationality
) {}
