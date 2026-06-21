package org.example.bookstore.service;

import org.example.bookstore.dto.BookRequest;
import org.example.bookstore.dto.BookResponse;
import org.example.bookstore.entity.Author;
import org.example.bookstore.entity.Book;
import org.example.bookstore.exception.ResourceNotFoundException;
import org.example.bookstore.mapper.BookMapper;
import org.example.bookstore.repository.AuthorRepository;
import org.example.bookstore.repository.BookRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class BookService {

    private final BookRepository bookRepository;
    private final AuthorRepository authorRepository;
    private final BookMapper bookMapper;

    public BookService(BookRepository bookRepository, AuthorRepository authorRepository, BookMapper bookMapper) {
        this.bookRepository = bookRepository;
        this.authorRepository = authorRepository;
        this.bookMapper = bookMapper;
    }

    public BookResponse create(BookRequest request) {
        Author author = findAuthorById(request.getAuthorId());
        Book book = bookMapper.toEntity(request);
        book.setAuthor(author);
        Book savedBook = bookRepository.save(book);
        return bookMapper.toResponse(savedBook);
    }

    @Transactional(readOnly = true)
    public BookResponse getById(Long id) {
        return bookMapper.toResponse(findBookById(id));
    }

    @Transactional(readOnly = true)
    public List<BookResponse> getAll() {
        return bookMapper.toResponseList(bookRepository.findAll());
    }

    public BookResponse update(Long id, BookRequest request) {
        Book book = findBookById(id);
        Author author = findAuthorById(request.getAuthorId());
        bookMapper.updateEntityFromRequest(request, book);
        book.setAuthor(author);
        Book savedBook = bookRepository.save(book);
        return bookMapper.toResponse(savedBook);
    }

    public void delete(Long id) {
        Book book = findBookById(id);
        bookRepository.delete(book);
    }

    private Book findBookById(Long id) {
        return bookRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Book with id " + id + " was not found"));
    }

    private Author findAuthorById(Long id) {
        return authorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Author with id " + id + " was not found"));
    }
}
