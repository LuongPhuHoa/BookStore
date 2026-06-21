package org.example.bookstore.service;

import org.example.bookstore.dto.AuthorRequest;
import org.example.bookstore.dto.AuthorResponse;
import org.example.bookstore.entity.Author;
import org.example.bookstore.exception.ResourceNotFoundException;
import org.example.bookstore.mapper.AuthorMapper;
import org.example.bookstore.repository.AuthorRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthorServiceTest {

    @Mock
    private AuthorRepository authorRepository;

    @Mock
    private AuthorMapper authorMapper;

    @InjectMocks
    private AuthorService authorService;

    @Test
    void createSavesAuthorAndReturnsResponse() {
        AuthorRequest request = new AuthorRequest("Robert Martin", "American");
        Author author = new Author(null, "Robert Martin", "American");
        Author savedAuthor = new Author(1L, "Robert Martin", "American");
        AuthorResponse response = new AuthorResponse();
        response.setId(1L);
        response.setName("Robert Martin");
        response.setNationality("American");

        when(authorMapper.toEntity(request)).thenReturn(author);
        when(authorRepository.save(author)).thenReturn(savedAuthor);
        when(authorMapper.toResponse(savedAuthor)).thenReturn(response);

        AuthorResponse result = authorService.create(request);

        assertEquals(1L, result.getId());
        assertEquals("Robert Martin", result.getName());
        verify(authorRepository).save(author);
    }

    @Test
    void getAllReturnsMappedAuthors() {
        Author author = new Author(1L, "Martin Fowler", "British");
        AuthorResponse response = new AuthorResponse();
        response.setId(1L);
        response.setName("Martin Fowler");

        when(authorRepository.findAll()).thenReturn(List.of(author));
        when(authorMapper.toResponseList(List.of(author))).thenReturn(List.of(response));

        List<AuthorResponse> result = authorService.getAll();

        assertEquals(1, result.size());
        assertEquals("Martin Fowler", result.getFirst().getName());
    }

    @Test
    void updateThrowsWhenAuthorDoesNotExist() {
        when(authorRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(
                ResourceNotFoundException.class,
                () -> authorService.update(99L, new AuthorRequest("Unknown", "Unknown"))
        );
    }
}
