package org.example.bookstore.controller;

import org.example.bookstore.dto.BookRequestDto;
import org.example.bookstore.dto.BookResponseDto;
import org.example.bookstore.service.BookService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/** BookController - HTTP REST endpoints for book operations. */
@RestController
@RequestMapping("/api/books")  // Base path for all endpoints
@RequiredArgsConstructor        // Generates constructor for BookService
public class BookController {
    private final BookService bookService;  // Injected by Spring

    /** Create a new book via HTTP POST. */
    @PostMapping  // POST /api/books
    public ResponseEntity<BookResponseDto> createBook(@RequestBody BookRequestDto requestDto) {
        // Call service to create book
        // Service handles:
        // - DTO → Entity mapping
        // - @Transactional transaction management
        // - Hibernate persistence
        // - Entity → DTO response mapping
        BookResponseDto response = bookService.createBook(requestDto);
        
        // Return 201 Created status with response body
        // REST convention: POST that creates returns 201 (not 200)
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /** Get a book by ID via HTTP GET. */
    @GetMapping("/{id}")  // GET /api/books/{id}
    public ResponseEntity<BookResponseDto> getBook(@PathVariable Long id) {
        // Call service to fetch book by ID
        // Service queries database and converts to DTO
        // If not found: throws IllegalArgumentException
        //   → GlobalExceptionHandler catches and returns 404
        BookResponseDto response = bookService.getBook(id);
        
        // Return 200 OK status with response body
        return ResponseEntity.ok(response);
    }
}

