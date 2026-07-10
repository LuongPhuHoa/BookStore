package org.example.bookstore.service;

import lombok.RequiredArgsConstructor;
import org.example.bookstore.dto.BookRequestDto;
import org.example.bookstore.dto.BookResponseDto;
import org.example.bookstore.entity.Author;
import org.example.bookstore.entity.Book;
import org.example.bookstore.mapper.BookMapper;
import org.example.bookstore.repository.AuthorRepository;
import org.example.bookstore.repository.BookRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

/**
 * BookService - Business logic and transaction boundaries for book operations.
 */
@Service
@RequiredArgsConstructor
public class BookService {
    private final BookRepository bookRepository;
    private final AuthorRepository authorRepository;
    private final BookMapper bookMapper;

    /**
     * Commit 02: this write method runs inside a transaction.
     * Commit 04: the Book is attached to an existing Author before it is saved.
     */
    @Transactional
    public BookResponseDto createBook(BookRequestDto requestDto) {
        Author author = authorRepository.findById(requestDto.authorId())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Author not found: " + requestDto.authorId()
                ));

        Book book = bookMapper.toEntity(requestDto);
        book.setAuthor(author);

        Book savedBook = bookRepository.save(book);
        return bookMapper.toResponseDto(savedBook);
    }

    /**
     * Commit 02: read-only transaction keeps lazy fields accessible during mapping.
     */
    @Transactional(readOnly = true)
    public BookResponseDto getBook(Long id) {
        return bookRepository.findById(id)
                .map(bookMapper::toResponseDto)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Book not found: " + id
                ));
    }

    /**
     * Retrieve all books. This is useful for observing relationship SELECT behavior.
     */
    @Transactional(readOnly = true)
    public List<BookResponseDto> getAllBooks() {
        return bookRepository.findAll().stream()
                .map(bookMapper::toResponseDto)
                .toList();
    }

    /**
     * Commit 02 lab: save a book, then fail so the transaction rolls back.
     */
    @Transactional
    public void createBookAndRollback(BookRequestDto requestDto) {
        createBook(requestDto);
        throw new IllegalStateException("Commit 02 rollback demo: transaction should roll back");
    }
}
