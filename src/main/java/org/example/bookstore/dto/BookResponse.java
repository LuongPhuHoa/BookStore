package org.example.bookstore.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Setter
@Getter
public class BookResponse {

    private Long id;

    private String title;

    private String isbn;

    private BigDecimal price;

    private Integer publishedYear;

    private Long authorId;

    private String authorName;

}
