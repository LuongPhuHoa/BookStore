package org.example.bookstore.mapper;

import org.example.bookstore.dto.BookRequestDto;
import org.example.bookstore.dto.BookResponseDto;
import org.example.bookstore.entity.Book;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/** BookMapper - Converts between Book entity and DTOs using MapStruct. */
@Mapper(componentModel = "spring")
public interface BookMapper {

    /**
     * Convert BookRequestDto (REST input) to Book entity (JPA entity).
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "author", ignore = true)
    Book toEntity(BookRequestDto dto);

    /**
     * Convert Book entity (from DB) to BookResponseDto (REST output).
     */
    @Mapping(target = "authorId", source = "author.id")
    @Mapping(target = "authorName", source = "author.name")
    BookResponseDto toResponseDto(Book entity);
}
