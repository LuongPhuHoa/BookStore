package org.example.bookstore.service;

import org.example.bookstore.dto.AuthorRequest;
import org.example.bookstore.dto.AuthorResponse;
import org.example.bookstore.entity.Author;
import org.example.bookstore.exception.ResourceNotFoundException;
import org.example.bookstore.mapper.AuthorMapper;
import org.example.bookstore.repository.AuthorRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class AuthorService {

    private final AuthorRepository authorRepository;
    private final AuthorMapper authorMapper;

    public AuthorService(AuthorRepository authorRepository, AuthorMapper authorMapper) {
        this.authorRepository = authorRepository;
        this.authorMapper = authorMapper;
    }

    public AuthorResponse create(AuthorRequest request) {
        Author author = authorMapper.toEntity(request);
        Author savedAuthor = authorRepository.save(author);
        return authorMapper.toResponse(savedAuthor);
    }

    @Transactional(readOnly = true)
    public AuthorResponse getById(Long id) {
        return authorMapper.toResponse(findAuthorById(id));
    }

    @Transactional(readOnly = true)
    public List<AuthorResponse> getAll() {
        return authorMapper.toResponseList(authorRepository.findAll());
    }

    public AuthorResponse update(Long id, AuthorRequest request) {
        Author author = findAuthorById(id);
        authorMapper.updateEntityFromRequest(request, author);
        Author savedAuthor = authorRepository.save(author);
        return authorMapper.toResponse(savedAuthor);
    }

    public void delete(Long id) {
        Author author = findAuthorById(id);
        authorRepository.delete(author);
    }

    private Author findAuthorById(Long id) {
        return authorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Author with id " + id + " was not found"));
    }
}
