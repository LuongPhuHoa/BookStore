package org.example.bookstore.config;

import org.example.bookstore.entity.Author;
import org.example.bookstore.entity.Book;
import org.example.bookstore.repository.AuthorRepository;
import org.example.bookstore.repository.BookRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.math.BigDecimal;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner loadSampleData(AuthorRepository authorRepository, BookRepository bookRepository) {
        return args -> {
            if (authorRepository.count() > 0) {
                return;
            }

            Author robertMartin = authorRepository.save(new Author(null, "Robert Martin", "American"));
            Author martinFowler = authorRepository.save(new Author(null, "Martin Fowler", "British"));

            bookRepository.save(new Book(
                    null,
                    "Clean Code",
                    "9780132350884",
                    new BigDecimal("45.99"),
                    2008,
                    robertMartin
            ));
            bookRepository.save(new Book(
                    null,
                    "Refactoring",
                    "9780134757599",
                    new BigDecimal("54.99"),
                    2018,
                    martinFowler
            ));
        };
    }
}
