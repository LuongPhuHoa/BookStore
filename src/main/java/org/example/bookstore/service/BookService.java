package org.example.bookstore.service;

import org.example.bookstore.dto.BookRequestDto;
import org.example.bookstore.dto.BookResponseDto;
import org.example.bookstore.entity.Book;
import org.example.bookstore.mapper.BookMapper;
import org.example.bookstore.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * BookService - Business logic for book operations.
 *
 * ═══════════════════════════════════════════════════════════════════════════════
 * RESPONSIBILITY:
 * ═══════════════════════════════════════════════════════════════════════════════
 * This service layer handles:
 * 1. Orchestrating BookRepository (database) and BookMapper (conversions)
 * 2. Defining transaction boundaries (@Transactional)
 * 3. Implementing business logic
 * 4. Transforming entities to DTOs for API responses
 * 5. Error handling and validation (in future commits)
 *
 * ═══════════════════════════════════════════════════════════════════════════════
 * LAYERED ARCHITECTURE:
 * ═══════════════════════════════════════════════════════════════════════════════
 *
 * CLIENT (REST)
 *    ↓
 * CONTROLLER (HTTP Handling)
 *    ├─ Receives HTTP request
 *    ├─ Deserializes JSON → DTO
 *    ├─ Validates input
 *    ├─ Delegates to SERVICE
 *    └─ Serializes response
 *
 * SERVICE (Business Logic) ← WE ARE HERE
 *    ├─ Doesn't know about HTTP
 *    ├─ Knows about Entity & Hibernateoperations
 *    ├─ Manages transactions
 *    ├─ Calls REPOSITORY
 *    └─ Uses MAPPER for conversions
 *
 * REPOSITORY (Database Access)
 *    ├─ Doesn't know about business logic
 *    ├─ Only database operations
 *    ├─ CRUD: save, find, delete, etc.
 *    └─ Returns Entity objects
 *
 * DATABASE (PostgreSQL)
 *    └─ Persisted data
 *
 * ═══════════════════════════════════════════════════════════════════════════════
 * WHY LAYERS?
 * ═══════════════════════════════════════════════════════════════════════════════
 * SEPARATION OF CONCERNS:
 * - Controller: Doesn't know database exists (REST protocol only)
 * - Service: Doesn't know HTTP exists (pure Java logic)
 * - Repository: Doesn't know business logic exists (only CRUD)
 *
 * TESTABILITY:
 * - Mock repository in service tests
 * - Mock service in controller tests
 * - Test each layer independently
 *
 * REUSABILITY:
 * - Service can be called from controller OR scheduled job OR queue consumer
 * - Repository can be called from service OR admin tools
 *
 * MAINTAINABILITY:
 * - Change database? Only modify repository
 * - Change business logic? Only modify service
 * - Change API format? Only modify controller
 *
 * ═══════════════════════════════════════════════════════════════════════════════
 * @RequiredArgsConstructor (Lombok):
 * ═══════════════════════════════════════════════════════════════════════════════
 * Generates a constructor for all final fields:
 *
 * Instead of:
 * public BookService(BookRepository repository, BookMapper mapper) {
 *     this.bookRepository = repository;
 *     this.bookMapper = mapper;
 * }
 *
 * Lombok generates the same code automatically.
 * Spring uses this constructor for dependency injection.
 *
 * @see org.springframework.stereotype.Service
 * @see org.springframework.transaction.annotation.Transactional
 */
@Service
@RequiredArgsConstructor  // Generates constructor for final fields (bookRepository, bookMapper)
public class BookService {
    // Final fields: can be injected via constructor, cannot change after initialization
    private final BookRepository bookRepository;
    private final BookMapper bookMapper;

    /**
     * Create and persist a new book.
     *
     * ═══════════════════════════════════════════════════════════════════════════════
     * TRANSACTION FLOW:
     * ═══════════════════════════════════════════════════════════════════════════════
     * @Transactional annotation manages the entire transaction lifecycle:
     *
     * 1. METHOD START:
     *    Spring detects @Transactional
     *    Opens database connection
     *    Starts transaction
     *    Creates EntityManager/Session (Hibernate context)
     *
     * 2. DTO → ENTITY CONVERSION (Line: Book book = ...)
     *    MapStruct generates code to create Book instance
     *    Sets title, isbn, price, publishedYear
     *    Sets id = null (not in DTO)
     *    Book is now TRANSIENT (not yet managed)
     *
     * 3. PERSISTENCE (Line: Book savedBook = ...)
     *    repository.save(book) called
     *    Hibernate adds Book to Persistence Context
     *    Book state: TRANSIENT → MANAGED
     *    No SQL executed yet (buffered in Hibernate)
     *
     * 4. ENTITY → DTO CONVERSION (Line: return bookMapper...)
     *    Book entity still has id = null? NO!
     *    Hibernate implicitly calls flush() before returning
     *    SQL INSERT executed: INSERT INTO books (...) VALUES (...)
     *    Database generates id
     *    Book.id populated with generated value (e.g., 1)
     *    BookResponseDto created with id = 1
     *
     * 5. METHOD END:
     *    Spring transaction proxy intercepts
     *    Calls commit() on database connection
     *    Any remaining data written
     *    EntityManager cleared (session ends)
     *    Connection released to pool
     *
     * ═══════════════════════════════════════════════════════════════════════════════
     * WHAT HIBERNATE DOES (Internally):
     * ═══════════════════════════════════════════════════════════════════════════════
     *
     * STEP-BY-STEP:
     * 1. save() is called
     *    - Checks if id is null
     *    - Yes → entity is new
     *    - Add to Persistence Context map
     *    - Key = Book@{id} or Book@{IdentityHashCode}
     *    - Value = Book instance
     *
     * 2. Dirty checking (continuous)
     *    - Hibernate stores initial state snapshot
     *    - If any field modified → entity marked dirty
     *    - On flush: generates UPDATE SQL for dirty entities
     *
     * 3. Flush (implicit or explicit)
     *    - DEFAULT: happens at transaction end
     *    - EXPLICIT: session.flush() or repository.saveAndFlush()
     *    - Generates SQL from entity state
     *
     * 4. Execute SQL
     *    - SQL sent to database via JDBC
     *    - For new entity (id=null): generates INSERT
     *    - For existing entity (id!=null): generates UPDATE
     *    - For deleted entity: generates DELETE
     *
     * 5. Retrieve generated keys
     *    - For INSERT with IDENTITY: database returns generated id
     *    - Hibernate updates entity.id in memory
     *
     * ═══════════════════════════════════════════════════════════════════════════════
     * EXPECTED SQL (Logged in console):
     * ═══════════════════════════════════════════════════════════════════════════════
     * When createBook() is called with:
     * title="Effective Java", isbn="978-0134685991", price=45.99, publishedYear=2018
     *
     * GENERATED SQL (with parameters shown):
     * Hibernate:
     *     INSERT INTO books (isbn, price, published_year, title)
     *     VALUES (?, ?, ?, ?)
     * [BIND: isbn='978-0134685991', price=45.99, published_year=2018, title='Effective Java']
     * [RESULT: Generated ID = 1]
     *
     * FORMATTED (for readability, when format_sql=true):
     * Hibernate:
     *     insert
     *     into
     *         books
     *         (isbn, price, published_year, title)
     *     values
     *         (?, ?, ?, ?)
     *
     * ═══════════════════════════════════════════════════════════════════════════════
     * WHY @Transactional IS CRITICAL:
     * ═══════════════════════════════════════════════════════════════════════════════
     * WITHOUT @Transactional:
     * - repository.save() would NOT execute SQL
     * - No transaction context available
     * - Exception: "No transaction is currently active"
     * - Even if it didn't throw, data wouldn't be persisted
     *
     * WITH @Transactional:
     * - Spring wraps method in proxy
     * - Proxy manages transaction (begin, commit, rollback)
     * - save() executes within transaction
     * - Flush happens automatically at end
     * - Data persisted to database
     *
     * ═══════════════════════════════════════════════════════════════════════════════
     * EXCEPTION HANDLING:
     * ═══════════════════════════════════════════════════════════════════════════════
     * If exception occurs during transaction:
     * 1. Spring proxy catches it
     * 2. Calls rollback() on connection
     * 3. All changes reverted (atomicity)
     * 4. Exception propagated to caller
     * 5. Caller's controller @ExceptionHandler catches it
     *
     * EXAMPLE:
     * try {
     *     bookService.createBook(requestDto);  // throws DataIntegrityViolationException
     * } catch (DataIntegrityViolationException e) {
     *     // Transaction already rolled back by Spring
     *     // Nothing was persisted
     *     // Return error response to client
     * }
     *
     * @param requestDto - book data from REST API (title, isbn, price, publishedYear)
     * @return BookResponseDto with generated ID
     *
     * @see org.springframework.transaction.annotation.Transactional
     * @see Commit 01: Entity Mapping
     * @see Commit 03: persist() Method
     * @see Commit 05: @Transactional Annotation
     */
    @Transactional  // CRITICAL: Enables Spring transaction management for this method
    public BookResponseDto createBook(BookRequestDto requestDto) {
        // STEP 1: Convert DTO to Entity
        // MapStruct (generated code) creates Book instance:
        // Book book = new Book();
        // book.setTitle(requestDto.title());
        // book.setIsbn(requestDto.isbn());
        // book.setPrice(requestDto.price());
        // book.setPublishedYear(requestDto.publishedYear());
        // book.setId(null);  // Not in DTO
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
        // At this point: savedBook.id is still null (INSERT not yet executed)
        //
        // However, when we call bookMapper.toResponseDto() below,
        // Spring's transaction proxy triggers implicit flush()
        // So by the time we return, the ID has been populated
        
        // STEP 3: Convert Entity back to DTO for response
        // MapStruct generates code to create BookResponseDto:
        // return new BookResponseDto(
        //     savedBook.getId(),        // ← Now has value (1, 2, 3, etc.)
        //     savedBook.getTitle(),
        //     savedBook.getIsbn(),
        //     savedBook.getPrice(),
        //     savedBook.getPublishedYear()
        // );
        return bookMapper.toResponseDto(savedBook);
        // After method returns:
        // - Spring transaction proxy detects end of method
        // - Calls flush() if not already done
        // - Calls commit() on database connection
        // - EntityManager/Session closed
        // - Book entity is now DETACHED (outside session)
    }

    /**
     * Retrieve a book by ID.
     *
     * ═══════════════════════════════════════════════════════════════════════════════
     * WHY NO @Transactional?
     * ═══════════════════════════════════════════════════════════════════════════════
     * Reading doesn't require transaction for data consistency.
     * (Though adding @Transactional doesn't hurt, it's optional)
     *
     * IMPORTANT: Don't read lazy-loaded relationships outside transaction!
     * If Book had a List<Author> with lazy loading:
     * - With transaction: Can access book.getAuthors() (loads inside transaction)
     * - Without transaction: LazyInitializationException (session already closed)
     * Current entity has no relationships, so this is safe.
     *
     * ═══════════════════════════════════════════════════════════════════════════════
     * EXPECTED SQL:
     * ═══════════════════════════════════════════════════════════════════════════════
     * When getBook(1) is called:
     *
     * Hibernate:
     *     SELECT b1_0.id,
     *         b1_0.isbn,
     *         b1_0.price,
     *         b1_0.published_year,
     *         b1_0.title
     *     FROM books b1_0
     *     WHERE b1_0.id = ?
     * [BIND: id=1]
     * [RESULT: Book(id=1, title=..., isbn=..., price=..., publishedYear=...)]
     *
     * ═══════════════════════════════════════════════════════════════════════════════
     * ENTITY LIFECYCLE DURING READ:
     * ═══════════════════════════════════════════════════════════════════════════════
     * 1. Spring creates implicit read-only transaction (or no transaction)
     * 2. findById() queries database
     * 3. Hibernate loads Book from ResultSet
     * 4. Book loaded into Persistence Context (MANAGED)
     * 5. Returns Optional<Book>
     * 6. map(bookMapper::toResponseDto) executes
     * 7. Converts Book to BookResponseDto
     * 8. Transaction ends
     * 9. Book becomes DETACHED
     *
     * @param id - book ID from URL (e.g., 1, 2, 3)
     * @return BookResponseDto with all fields
     * @throws IllegalArgumentException if book not found (custom error message)
     */
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
