package org.example.bookstore.controller;

import org.example.bookstore.dto.BookRequestDto;
import org.example.bookstore.dto.BookResponseDto;
import org.example.bookstore.service.BookService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/lab")
@RequiredArgsConstructor
public class LabController {
    private final BookService bookService;

    @PostMapping("/create-book")
    public ResponseEntity<BookResponseDto> createBook(@RequestBody BookRequestDto requestDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(bookService.createBook(requestDto));
    }

    @GetMapping("/get-book/{id}")
    public ResponseEntity<BookResponseDto> getBook(@PathVariable Long id) {
        return ResponseEntity.ok(bookService.getBook(id));
    }
}
