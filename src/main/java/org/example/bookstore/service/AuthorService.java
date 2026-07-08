package org.example.bookstore.service;

import lombok.RequiredArgsConstructor;
import org.example.bookstore.dto.AuthorRequestDto;
import org.example.bookstore.dto.AuthorResponseDto;
import org.example.bookstore.entity.Author;
import org.example.bookstore.exception.ResourceNotFoundException;
import org.example.bookstore.mapper.AuthorMapper;
import org.example.bookstore.repository.AuthorRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Service providing simple CRUD for Author used in lab controllers.
 * Keeps business logic minimal so lab focuses on JPA behavior.
 */
@Service
@RequiredArgsConstructor
public class AuthorService {
    private final AuthorRepository authorRepository;
    private final AuthorMapper authorMapper;

    public AuthorResponseDto create(AuthorRequestDto dto) {
        Author author = authorMapper.toEntity(dto);
        Author saved = authorRepository.save(author);
        return authorMapper.toResponseDto(saved);
    }

    public List<AuthorResponseDto> getAll() {
        return authorRepository.findAll().stream()
                .map(authorMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    public AuthorResponseDto getById(Long id) {
        return authorRepository.findById(id)
                .map(authorMapper::toResponseDto)
                .orElseThrow(() -> new ResourceNotFoundException("Author not found: " + id));
    }

    public AuthorResponseDto update(Long id, AuthorRequestDto dto) {
        Author author = authorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Author not found: " + id));
        author.setName(dto.name());
        author.setNationality(dto.nationality());
        Author saved = authorRepository.save(author);
        return authorMapper.toResponseDto(saved);
    }

    public void delete(Long id) {
        if (!authorRepository.existsById(id)) {
            throw new ResourceNotFoundException("Author not found: " + id);
        }
        authorRepository.deleteById(id);
    }
}