package org.example.bookstore.service;

import org.example.bookstore.dto.BookRequestDto;
import org.example.bookstore.dto.BookResponseDto;
import org.example.bookstore.entity.Book;
import org.example.bookstore.mapper.BookMapper;
import org.example.bookstore.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class BookService {
    private final BookRepository bookRepository;
    private final BookMapper bookMapper;

    @Transactional
    public BookResponseDto createBook(BookRequestDto requestDto) {
        Book book = bookMapper.toEntity(requestDto);
        Book savedBook = bookRepository.save(book);
        return bookMapper.toResponseDto(savedBook);
    }

    public BookResponseDto getBook(Long id) {
        return bookRepository.findById(id)
            .map(bookMapper::toResponseDto)
            .orElseThrow(() -> new IllegalArgumentException("Book not found: " + id));
    }
}
