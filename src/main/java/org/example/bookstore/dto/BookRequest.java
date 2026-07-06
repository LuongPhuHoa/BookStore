package org.example.bookstore.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Setter
@Getter
public class BookRequest {

    @NotBlank(message = "Book title is required")
    private String title;

    @NotBlank(message = "Book ISBN is required")
    private String isbn;

    @NotNull(message = "Book price is required")
    @Positive(message = "Book price must be positive")
    private BigDecimal price;

    @NotNull(message = "Published year is required")
    @Positive(message = "Published year must be positive")
    private Integer publishedYear;

    @NotNull(message = "Author id is required")
    private Long authorId;

}
