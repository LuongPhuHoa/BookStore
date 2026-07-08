package org.example.bookstore.service;

import org.example.bookstore.dto.BookRequestDto;
import org.example.bookstore.dto.BookResponseDto;
import org.example.bookstore.entity.Book;
import org.example.bookstore.mapper.BookMapper;
import org.example.bookstore.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/** BookService - Business logic for book operations. */
@Service
@RequiredArgsConstructor  // Generates constructor for final fields (bookRepository, bookMapper)
public class BookService {
    // Final fields: can be injected via constructor, cannot change after initialization
    private final BookRepository bookRepository;
    private final BookMapper bookMapper;

    /** Create and persist a new book. */
    @Transactional  // CRITICAL: Enables Spring transaction management for this method
    public BookResponseDto createBook(BookRequestDto requestDto) {
        /*
         STEP 1: Convert DTO to Entity
         MapStruct (generated code) creates Book instance:
                Book book = new Book();
                book.setTitle(requestDto.title());
                book.setIsbn(requestDto.isbn());
                book.setPrice(requestDto.price());
                book.setPublishedYear(requestDto.publishedYear());
                book.setId(null);  // Not in DTO
        */
        Book book = bookMapper.toEntity(requestDto);
        
        // STEP 2: Persist entity to database
        // Note: save() does NOT immediately execute INSERT SQL
        // It only adds entity to Persistence Context (MANAGED state)
        // SQL will be generated and executed when transaction ends (automatic flush)
        //
        // Behind the scenes:
        // - Hibernate checks if id is null
        // - Yes → entity is new
        // - Add Book instance to Persistence Context
        // - Enable dirty checking on this entity
        Book savedBook = bookRepository.save(book);
        /*
         At this point: savedBook.id is still null (INSERT not yet executed)

         However, when we call bookMapper.toResponseDto() below,
         Spring's transaction proxy triggers implicit flush()
         So by the time we return, the ID has been populated
         STEP 3: Convert Entity back to DTO for response
         MapStruct generates code to create BookResponseDto:
                 return new BookResponseDto(
                     savedBook.getId(),        // ← Now has value (1, 2, 3, etc.)
                     savedBook.getTitle(),
                     savedBook.getIsbn(),
                     savedBook.getPrice(),
                     savedBook.getPublishedYear()
                 );
        */

        return bookMapper.toResponseDto(savedBook);
        // After method returns:
        // - Spring transaction proxy detects end of method
        // - Calls flush() if not already done
        // - Calls commit() on database connection
        // - EntityManager/Session closed
        // - Book entity is now DETACHED (outside session)
    }

    /** Retrieve a book by ID. */
    public BookResponseDto getBook(Long id) {
        // findById() returns Optional<Book>
        // If found: wraps in Optional.of(book)
        // If not found: returns Optional.empty()
        return bookRepository.findById(id)
            // map() executes only if Optional contains value
            // Converts Book → BookResponseDto via MapStruct
            .map(bookMapper::toResponseDto)
            // orElseThrow() called if Optional is empty
            // Throws IllegalArgumentException with custom message
            .orElseThrow(() -> new IllegalArgumentException("Book not found: " + id));
            // Better practice (future commits) would throw:
            // new ResourceNotFoundException("Book with ID " + id + " not found")
    }
}

