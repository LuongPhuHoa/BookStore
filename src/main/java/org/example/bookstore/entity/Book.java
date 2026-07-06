package org.example.bookstore.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@Entity
@Table(name = "books")
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Long id;

    private String title;

    private String isbn;

    private BigDecimal price;

    private Integer publishedYear;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id", nullable = false)
    private Author author;

    public Book() {
    }

    public Book(Long id, String title, String isbn, BigDecimal price, Integer publishedYear, Author author) {
        this.id = id;
        this.title = title;
        this.isbn = isbn;
        this.price = price;
        this.publishedYear = publishedYear;
        this.author = author;
    }

}
