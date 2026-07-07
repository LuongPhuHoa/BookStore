package org.example.bookstore.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

/**
 * Book Entity - Represents a book in the library management system.
 *
 * ═══════════════════════════════════════════════════════════════════════════════
 * LEARNING OBJECTIVE (Commit 01):
 * ═══════════════════════════════════════════════════════════════════════════════
 * This entity demonstrates BASIC ENTITY MAPPING and JPA fundamentals.
 * You'll observe how Hibernate converts Java objects into relational database tables.
 *
 * ═══════════════════════════════════════════════════════════════════════════════
 * KEY CONCEPTS:
 * ═══════════════════════════════════════════════════════════════════════════════
 *
 * 1. @Entity
 *    - Marks this class as a JPA entity
 *    - Tells Hibernate: "This class maps to a database table"
 *    - Hibernate scans @Entity classes at startup and builds metadata
 *    - Without this, the class is just a POJO with no database mapping
 *
 * 2. @Table(name = "books")
 *    - Explicitly maps this class to the 'books' table
 *    - If omitted, Hibernate uses lowercase class name: 'book' (NOT recommended)
 *    - Allows custom table naming (useful for legacy databases)
 *
 * 3. @Id
 *    - Marks this field as the primary key
 *    - JPA requires exactly ONE @Id per entity
 *    - Can be single-column (@Id) or composite (@IdClass or @EmbeddedId)
 *
 * 4. @GeneratedValue(strategy = GenerationType.IDENTITY)
 *    - Tells Hibernate HOW to generate ID values
 *    - IDENTITY strategy: Let the database auto-increment (PostgreSQL: SERIAL/BIGSERIAL)
 *    - Alternative strategies:
 *      * GenerationType.SEQUENCE (better for portability)
 *      * GenerationType.AUTO (Hibernate picks the best)
 *      * GenerationType.TABLE (portable, but slower)
 *
 * 5. @Column Annotations
 *    - Customizes column mapping
 *    - nullable = false: NOT NULL constraint in DDL
 *    - unique = true: UNIQUE constraint in DDL
 *    - name: Custom column name (otherwise uses field name)
 *
 * 6. Default Constructor (NoArgsConstructor)
 *    - REQUIRED by Hibernate for reflection and object instantiation
 *    - When fetching from database, Hibernate uses reflection:
 *      1. Instantiates Book() with no-args constructor
 *      2. Uses reflection to set field values
 *    - If you remove NoArgsConstructor, Hibernate will throw InstantiationException
 *    - Can be private if AllArgsConstructor is present
 *
 * ═══════════════════════════════════════════════════════════════════════════════
 * WHAT HIBERNATE DOES AT STARTUP:
 * ═══════════════════════════════════════════════════════════════════════════════
 * 1. Scans classpath for @Entity classes
 * 2. Reads annotations and builds metadata (field names, types, constraints)
 * 3. Generates DDL (Data Definition Language) based on ddl-auto setting
 * 4. If ddl-auto=create: Drops and recreates all tables
 * 5. Creates database connection pool
 * 6. Registers EntityManager
 *
 * ═══════════════════════════════════════════════════════════════════════════════
 * EXPECTED SQL (DDL - executed at application startup):
 * ═══════════════════════════════════════════════════════════════════════════════
 * DROP TABLE IF EXISTS books CASCADE;
 *
 * CREATE TABLE books (
 *     id BIGSERIAL PRIMARY KEY,
 *     isbn VARCHAR(255) NOT NULL UNIQUE,
 *     price NUMERIC NOT NULL,
 *     published_year INTEGER,
 *     title VARCHAR(255) NOT NULL
 * );
 *
 * Notes:
 * - BIGSERIAL: Auto-incrementing 64-bit integer (for IDENTITY strategy)
 * - PRIMARY KEY: id is the unique identifier
 * - NOT NULL: Enforces non-null constraint from @Column(nullable=false)
 * - UNIQUE: Enforces uniqueness constraint from @Column(unique=true)
 * - VARCHAR(255): Default length for String fields
 * - NUMERIC: Decimal type for BigDecimal fields
 *
 * ═══════════════════════════════════════════════════════════════════════════════
 * ENTITY LIFECYCLE (CRUD operations):
 * ═══════════════════════════════════════════════════════════════════════════════
 * 1. TRANSIENT (not yet managed)
 *    - Book book = new Book("Title", "isbn", price, year)
 *    - Not in Persistence Context
 *    - Changes won't be tracked
 *
 * 2. MANAGED (in Persistence Context)
 *    - repository.save(book) adds to Persistence Context
 *    - Hibernate tracks ALL changes to this instance
 *    - If fields change, Hibernate generates UPDATE on flush
 *
 * 3. DETACHED (was managed, now outside transaction)
 *    - After transaction ends
 *    - Changes won't be tracked (LazyInitializationException if accessing relationships)
 *
 * 4. REMOVED (marked for deletion)
 *    - repository.delete(book)
 *    - Next flush() executes DELETE SQL
 *
 * ═══════════════════════════════════════════════════════════════════════════════
 * COMMON MISTAKES (and why they fail):
 * ═══════════════════════════════════════════════════════════════════════════════
 * ❌ Remove @Entity
 *    → Result: No mapping occurs, INSERT statements won't be generated
 *
 * ❌ Remove default constructor (NoArgsConstructor)
 *    → Result: InstantiationException when fetching from database
 *    → Hibernate can't instantiate: Book() without arguments
 *
 * ❌ Remove @Table annotation
 *    → Result: Uses lowercase class name 'book' instead of 'books'
 *    → Still works, but creates wrong table name
 *
 * ❌ Remove @GeneratedValue
 *    → Result: Must manually set ID before saving
 *    → If omitted and you don't set ID: Database constraint violation
 *
 * ❌ Modify entity inside @Transactional without setter
 *    → Result: Dirty checking might miss the change
 *    → Use setter methods so Hibernate can track changes
 *
 * ═══════════════════════════════════════════════════════════════════════════════
 * PRODUCTION VS LEARNING:
 * ═══════════════════════════════════════════════════════════════════════════════
 * Learning:   ddl-auto=create  (recreate schema on every startup)
 * Production: ddl-auto=validate (verify schema exists, don't modify)
 * Staging:    ddl-auto=update  (create new tables/columns if needed)
 *
 * @see jakarta.persistence.Entity
 * @see jakarta.persistence.Table
 * @see jakarta.persistence.Id
 * @see jakarta.persistence.GeneratedValue
 * @see jakarta.persistence.Column
 */
@Entity
@Table(name = "books")
@Getter                 // Lombok: generates all getters
@Setter                 // Lombok: generates all setters
@NoArgsConstructor      // Lombok: generates public no-args constructor (REQUIRED by Hibernate)
@AllArgsConstructor     // Lombok: generates constructor with all fields (useful for testing)
public class Book {
    
    /**
     * Primary key field (database ID).
     *
     * - @Id marks this as the primary key
     * - @GeneratedValue(IDENTITY) tells PostgreSQL to auto-increment
     * - Type: Long (64-bit) allows IDs up to 9.2 quintillion
     * - Will be null until saved to database (then auto-assigned)
     *
     * INSERTION FLOW:
     * 1. Create new Book() → id = null
     * 2. repository.save(book) → Hibernate adds to context
     * 3. Transaction commits → INSERT SQL generated
     * 4. Database auto-increments → id = 1
     * 5. Hibernate updates in-memory instance: book.id = 1
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Book title (required, not null).
     *
     * - @Column(nullable = false) creates NOT NULL constraint
     * - Hibernate validates: if null, throws ConstraintViolationException
     * - VARCHAR(255): Default max length for String fields
     *
     * EXAMPLE SQL:
     * INSERT INTO books (title, isbn, price, published_year)
     * VALUES (?, ?, ?, ?)  ← Placeholder for title
     */
    @Column(nullable = false)
    private String title;

    /**
     * ISBN (International Standard Book Number) - unique identifier.
     *
     * - @Column(nullable = false, unique = true)
     * - Creates UNIQUE constraint: no two books can have same ISBN
     * - Database enforces uniqueness → duplicate inserts fail
     * - Helpful for data integrity (no duplicate books)
     *
     * INSERTION CONFLICT:
     * If you try to save two books with same ISBN:
     * INSERT INTO books (isbn, ...) VALUES ('978-0134685991', ...)  ✓ First succeeds
     * INSERT INTO books (isbn, ...) VALUES ('978-0134685991', ...)  ✗ Second fails
     * → DataIntegrityViolationException
     */
    @Column(nullable = false, unique = true)
    private String isbn;

    /**
     * Book price (required).
     *
     * - Type: BigDecimal (for precise decimal arithmetic)
     * - Use BigDecimal for money, NOT double (floating-point rounding errors)
     * - @Column(nullable = false) creates NOT NULL constraint
     * - Maps to PostgreSQL NUMERIC type (arbitrary precision)
     *
     * WHY NOT double?
     * double price = 0.1 + 0.2;  // = 0.30000000000000004 ❌
     * Better:
     * BigDecimal price = BigDecimal.valueOf(0.1).add(BigDecimal.valueOf(0.2));
     */
    @Column(nullable = false)
    private BigDecimal price;

    /**
     * Year the book was published (optional).
     *
     * - Type: Integer (not int) because NULL is allowed
     * - No @Column(nullable=false) → Can be null
     * - @Column(name="published_year") maps to snake_case database column
     * - Defaults to null if not provided in request
     *
     * WHY map to "published_year"?
     * - Database convention: snake_case for multi-word columns
     * - Java convention: camelCase for field names
     * - This annotation bridges the two conventions
     */
    @Column(name = "published_year")
    private Integer publishedYear;
}
