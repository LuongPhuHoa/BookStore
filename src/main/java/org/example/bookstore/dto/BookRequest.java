package org.example.bookstore.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Integer getPublishedYear() {
        return publishedYear;
    }

    public void setPublishedYear(Integer publishedYear) {
        this.publishedYear = publishedYear;
    }

    public Long getAuthorId() {
        return authorId;
    }

    public void setAuthorId(Long authorId) {
        this.authorId = authorId;
    }
}
