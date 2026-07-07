package org.example.bookstore.dto;

import java.math.BigDecimal;

public record BookResponseDto(
    Long id,
    String title,
    String isbn,
    BigDecimal price,
    Integer publishedYear
) {}
