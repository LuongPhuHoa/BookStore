package org.example.bookstore.dto;

import java.math.BigDecimal;

public record BookRequestDto(
    String title,
    String isbn,
    BigDecimal price,
    Integer publishedYear
) {}
