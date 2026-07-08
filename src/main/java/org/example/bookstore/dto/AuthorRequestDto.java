package org.example.bookstore.dto;

/** Compact input DTO for Author create/update requests. */
public record AuthorRequestDto(
    String name,
    String nationality
) {}