package org.example.bookstore;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/** BookStoreApplication - Main entry point for the Hibernate Learning Laboratory. */
@SpringBootApplication  // Enables @Configuration + @ComponentScan + @EnableAutoConfiguration
public class BookStoreApplication {

    /** Main entry point for the Spring Boot application. */
    public static void main(String[] args) {
        // SpringApplication.run() is the magic line that starts everything
        // First argument: this class (BookStoreApplication.class)
        // Second argument: command-line args (if any)
        SpringApplication.run(BookStoreApplication.class, args);
    }
}

