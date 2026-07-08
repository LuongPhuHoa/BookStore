package org.example.bookstore.mapper;

import org.mapstruct.Mapper;
import org.example.bookstore.entity.Author;
import org.example.bookstore.dto.AuthorRequestDto;
import org.example.bookstore.dto.AuthorResponseDto;

/** MapStruct mapper for Author conversions. */
@Mapper(componentModel = "spring")
public interface AuthorMapper {
    Author toEntity(AuthorRequestDto dto);
    AuthorResponseDto toResponseDto(Author entity);
}
