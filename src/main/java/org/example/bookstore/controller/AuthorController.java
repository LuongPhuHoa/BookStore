package org.example.bookstore.controller;

import lombok.RequiredArgsConstructor;
import org.example.bookstore.dto.AuthorRequestDto;
import org.example.bookstore.dto.AuthorResponseDto;
import org.example.bookstore.service.AuthorService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Lab controller exposing simple CRUD for Author under /lab/authors.
 */
@RestController
@RequestMapping("/lab/authors")
@RequiredArgsConstructor
public class AuthorController {
    private final AuthorService authorService;

    @PostMapping
    public ResponseEntity<AuthorResponseDto> create(@RequestBody AuthorRequestDto dto) {
        AuthorResponseDto resp = authorService.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(resp);
    }

    @GetMapping
    public List<AuthorResponseDto> list() {
        return authorService.getAll();
    }

    @GetMapping("/{id}")
    public AuthorResponseDto get(@PathVariable Long id) {
        return authorService.getById(id);
    }

    @PutMapping("/{id}")
    public AuthorResponseDto update(@PathVariable Long id, @RequestBody AuthorRequestDto dto) {
        return authorService.update(id, dto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        authorService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
