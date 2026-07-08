package org.example.bookstore.repository;

import org.example.bookstore.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;

/** BookRepository - Spring Data JPA repository for Book entity. */
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

