package org.example.bookstore.dto;

/** Compact response DTO returned to clients. */
public record AuthorResponseDto(
    Long id,
    String name,
    String nationality
) {}