package org.example.bookstore.repository;

import org.example.bookstore.entity.Author;
import org.springframework.data.jpa.repository.JpaRepository;

/** Repository for Author entities. */
public interface AuthorRepository extends JpaRepository<Author, Long> {
}
