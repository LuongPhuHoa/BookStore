package org.example.bookstore.controller;

import org.example.bookstore.dto.BookRequestDto;
import org.example.bookstore.dto.BookResponseDto;
import org.example.bookstore.service.BookService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/** LabController - Experimental endpoints for learning Hibernate internals. */
@RestController
@RequestMapping("/lab")  // Base path: /lab
@RequiredArgsConstructor
public class LabController {
    private final BookService bookService;

    /** Create a book and observe entity lifecycle + SQL generation. */
    @PostMapping("/create-book")
    public ResponseEntity<BookResponseDto> createBook(@RequestBody BookRequestDto requestDto) {
        System.out.println("═══════════════════════════════════════════════════════════════");
        System.out.println("LAB ENDPOINT: createBook() invoked");
        System.out.println("Request DTO: " + requestDto);
        System.out.println("═══════════════════════════════════════════════════════════════");
        System.out.println("STEP 1: Calling bookService.createBook()...");
        System.out.println("        @Transactional will START a transaction here");
        
        // Call service (which is @Transactional)
        // This is where the magic happens!
        BookResponseDto response = bookService.createBook(requestDto);
        
        System.out.println("STEP 2: bookService.createBook() returned");
        System.out.println("        Response DTO: " + response);
        System.out.println("        Book ID is now: " + response.id());
        System.out.println("        @Transactional will COMMIT the transaction here");
        System.out.println("═══════════════════════════════════════════════════════════════");
        System.out.println("LAB ENDPOINT: createBook() complete!");
        System.out.println("═══════════════════════════════════════════════════════════════\n");
        
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /** Retrieve a book and observe SELECT query. */
    @GetMapping("/get-book/{id}")
    public ResponseEntity<BookResponseDto> getBook(@PathVariable Long id) {
        System.out.println("═══════════════════════════════════════════════════════════════");
        System.out.println("LAB ENDPOINT: getBook(" + id + ") invoked");
        System.out.println("═══════════════════════════════════════════════════════════════");
        System.out.println("STEP 1: Calling bookService.getBook(" + id + ")...");
        
        // Call service to fetch book
        BookResponseDto response = bookService.getBook(id);
        
        System.out.println("STEP 2: bookService.getBook() returned");
        System.out.println("        Response DTO: " + response);
        System.out.println("═══════════════════════════════════════════════════════════════");
        System.out.println("LAB ENDPOINT: getBook() complete!");
        System.out.println("═══════════════════════════════════════════════════════════════\n");
        
        return ResponseEntity.ok(response);
    }
}

