package org.example.bookstore.repository;

import org.example.bookstore.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * BookRepository - Spring Data JPA repository for Book entity.
 *
 * ═══════════════════════════════════════════════════════════════════════════════
 * WHAT IS JPAREPOSITORY?
 * ═══════════════════════════════════════════════════════════════════════════════
 * JpaRepository is a generic interface that provides common CRUD operations.
 * It's part of Spring Data JPA (reduces boilerplate).
 *
 * INHERITANCE HIERARCHY:
 * JpaRepository
 *   ↓
 * PagingAndSortingRepository
 *   ↓
 * CrudRepository
 *   ↓
 * Repository (marker interface)
 *
 * Each level adds more functionality:
 * - Repository: Marker interface (no methods)
 * - CrudRepository: CRUD operations (save, delete, findById, etc.)
 * - PagingAndSortingRepository: Pagination & sorting
 * - JpaRepository: Batch operations, flush, transaction management
 *
 * ═══════════════════════════════════════════════════════════════════════════════
 * GENERIC PARAMETERS:
 * ═══════════════════════════════════════════════════════════════════════════════
 * JpaRepository<Book, Long>
 *             ↑     ↑
 *         Entity  Primary Key Type
 *
 * - Book: The entity class this repository manages
 * - Long: The type of the primary key field (@Id type)
 *
 * ═══════════════════════════════════════════════════════════════════════════════
 * METHODS PROVIDED BY JPAREPOSITORY:
 * ═══════════════════════════════════════════════════════════════════════════════
 *
 * BASIC CRUD:
 * 1. save(Book entity) → Book
 *    - Inserts new entity (if id = null)
 *    - Updates existing entity (if id != null)
 *    - SQL: INSERT or UPDATE
 *
 * 2. findById(Long id) → Optional<Book>
 *    - Retrieves by primary key
 *    - SQL: SELECT * FROM books WHERE id = ?
 *    - Returns Optional (empty if not found)
 *
 * 3. findAll() → List<Book>
 *    - Retrieves all entities
 *    - SQL: SELECT * FROM books
 *    - Returns List (empty if no data)
 *
 * 4. delete(Book entity) → void
 *    - Deletes the given entity
 *    - SQL: DELETE FROM books WHERE id = ?
 *    - Entity must be in Persistence Context
 *
 * 5. deleteById(Long id) → void
 *    - Deletes by primary key
 *    - SQL: DELETE FROM books WHERE id = ?
 *
 * 6. count() → long
 *    - Returns number of entities
 *    - SQL: SELECT COUNT(*) FROM books
 *
 * 7. exists(Long id) → boolean
 *    - Checks if entity exists
 *    - SQL: SELECT 1 FROM books WHERE id = ? LIMIT 1
 *
 * BATCH OPERATIONS (from JpaRepository):
 * 8. saveAll(Iterable<Book> entities) → List<Book>
 *    - Saves multiple entities efficiently
 *    - SQL: Multiple INSERT statements
 *
 * 9. deleteAll() → void
 *    - Deletes all entities
 *    - SQL: DELETE FROM books
 *
 * 10. deleteAll(Iterable<Book> entities) → void
 *     - Deletes multiple entities
 *
 * ═══════════════════════════════════════════════════════════════════════════════
 * HOW SPRING DATA JPA WORKS:
 * ═══════════════════════════════════════════════════════════════════════════════
 * 1. APPLICATION STARTUP:
 *    - Spring detects interfaces extending JpaRepository
 *    - Uses proxy pattern to create implementation at runtime
 *    - Registers as Spring bean (@Repository stereotype)
 *
 * 2. DEPENDENCY INJECTION:
 *    @Service
 *    public class BookService {
 *        @Autowired  // Spring injects BookRepository implementation
 *        private BookRepository bookRepository;
 *    }
 *
 * 3. METHOD INVOCATION:
 *    When you call bookRepository.save(book):
 *    - Proxy intercepts the call
 *    - Translates to SQL INSERT/UPDATE
 *    - Sends to database via JDBC
 *    - Returns result
 *
 * ═══════════════════════════════════════════════════════════════════════════════
 * TRANSACTION BOUNDARIES:
 * ═══════════════════════════════════════════════════════════════════════════════
 * Spring Data JPA repository methods are transactional:
 *
 * @Service
 * public class BookService {
 *     @Transactional  // Entire method wrapped in transaction
 *     public BookResponseDto createBook(BookRequestDto requestDto) {
 *         Book book = bookMapper.toEntity(requestDto);
 *         Book savedBook = repository.save(book);  // ← Inside transaction
 *         return bookMapper.toResponseDto(savedBook);
 *     }
 *     // ← At the end, transaction commits, flush() called automatically
 * }
 *
 * WITHOUT @Transactional: save() would not persist to database
 * Repository.save() requires an active transaction
 *
 * ═══════════════════════════════════════════════════════════════════════════════
 * PERSISTENCE CONTEXT & SESSION:
 * ═══════════════════════════════════════════════════════════════════════════════
 * When you save an entity:
 * 1. save(book) → Hibernate adds to Persistence Context (MANAGED state)
 * 2. Entity tracked for changes (dirty checking enabled)
 * 3. At transaction commit → flush() called
 * 4. flush() → Generates and executes SQL
 * 5. Persistence Context cleared (optional, based on transaction strategy)
 *
 * ═══════════════════════════════════════════════════════════════════════════════
 * CUSTOM QUERIES:
 * ═══════════════════════════════════════════════════════════════════════════════
 * Spring Data JPA provides query generation by method naming.
 * For complex queries, use @Query annotation:
 *
 * FUTURE COMMITS (Commit 17) will add:
 * @Query("SELECT b FROM Book b WHERE b.isbn = :isbn")
 * Optional<Book> findByIsbn(@Param("isbn") String isbn);
 *
 * @Query("SELECT b FROM Book b WHERE b.price > :minPrice")
 * List<Book> findExpensiveBooks(@Param("minPrice") BigDecimal minPrice);
 *
 * Or use method naming convention:
 * Optional<Book> findByIsbn(String isbn);  // Auto-generates WHERE clause
 * List<Book> findByPriceGreaterThan(BigDecimal price);
 *
 * Spring Data JPA automatically generates SQL for these method names.
 *
 * ═══════════════════════════════════════════════════════════════════════════════
 * BEST PRACTICES:
 * ═══════════════════════════════════════════════════════════════════════════════
 * ✅ Always wrap repository calls in @Transactional (service layer)
 * ✅ Use custom queries (@Query) for complex logic
 * ✅ Paginate large result sets (use Pageable interface)
 * ✅ Use batch operations for bulk inserts (saveAll)
 * ✅ Add indexes to frequently queried columns
 * ❌ Don't call repository from controller directly
 * ❌ Don't call repository without @Transactional
 * ❌ Don't modify detached entities (they won't update)
 *
 * @see org.springframework.data.jpa.repository.JpaRepository
 * @see org.springframework.stereotype.Repository
 * @see org.springframework.transaction.annotation.Transactional
 */
public interface BookRepository extends JpaRepository<Book, Long> {
    // Pre-defined methods inherited from JpaRepository (no need to implement):
    //
    // CRUD OPERATIONS (inherited):
    // - Book save(Book entity)
    // - Optional<Book> findById(Long id)
    // - List<Book> findAll()
    // - void delete(Book entity)
    // - void deleteById(Long id)
    // - long count()
    // - boolean existsById(Long id)
    //
    // BATCH OPERATIONS (inherited):
    // - List<Book> saveAll(Iterable<Book> entities)
    // - void deleteAll()
    // - void deleteAll(Iterable<Book> entities)
    // - List<Book> findAllById(Iterable<Long> ids)
    //
    // PAGINATION & SORTING (from PagingAndSortingRepository):
    // - Page<Book> findAll(Pageable pageable)
    // - List<Book> findAll(Sort sort)
    //
    // Add custom queries below for Commit 17:
    // @Query("SELECT b FROM Book b WHERE b.isbn = ?1")
    // Optional<Book> findByIsbn(String isbn);
    //
    // @Query("SELECT b FROM Book b WHERE b.price > ?1 ORDER BY b.price DESC")
    // List<Book> findExpensiveBooks(BigDecimal minPrice);
}
