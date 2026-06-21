package org.example.bookstore.service;

import org.example.bookstore.dto.BookRequest;
import org.example.bookstore.dto.BookResponse;
import org.example.bookstore.entity.Author;
import org.example.bookstore.entity.Book;
import org.example.bookstore.exception.ResourceNotFoundException;
import org.example.bookstore.mapper.BookMapper;
import org.example.bookstore.repository.AuthorRepository;
import org.example.bookstore.repository.BookRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BookServiceTest {

    @Mock
    private BookRepository bookRepository;

    @Mock
    private AuthorRepository authorRepository;

    @Mock
    private BookMapper bookMapper;

    @InjectMocks
    private BookService bookService;

    @Test
    void createLinksAuthorBeforeSavingBook() {
        BookRequest request = createBookRequest();
        Author author = new Author(1L, "Robert Martin", "American");
        Book book = new Book();
        Book savedBook = new Book(10L, "Clean Code", "9780132350884", new BigDecimal("45.99"), 2008, author);
        BookResponse response = new BookResponse();
        response.setId(10L);
        response.setTitle("Clean Code");
        response.setAuthorId(1L);

        when(authorRepository.findById(1L)).thenReturn(Optional.of(author));
        when(bookMapper.toEntity(request)).thenReturn(book);
        when(bookRepository.save(book)).thenReturn(savedBook);
        when(bookMapper.toResponse(savedBook)).thenReturn(response);

        BookResponse result = bookService.create(request);

        assertEquals(10L, result.getId());
        assertEquals(1L, book.getAuthor().getId());
        verify(bookRepository).save(book);
    }

    @Test
    void getByIdThrowsWhenBookDoesNotExist() {
        when(bookRepository.findById(404L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> bookService.getById(404L));
    }

    @Test
    void updateThrowsWhenNewAuthorDoesNotExist() {
        BookRequest request = createBookRequest();
        Book existingBook = new Book();

        when(bookRepository.findById(10L)).thenReturn(Optional.of(existingBook));
        when(authorRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> bookService.update(10L, request));
    }

    private BookRequest createBookRequest() {
        BookRequest request = new BookRequest();
        request.setTitle("Clean Code");
        request.setIsbn("9780132350884");
        request.setPrice(new BigDecimal("45.99"));
        request.setPublishedYear(2008);
        request.setAuthorId(1L);
        return request;
    }
}
