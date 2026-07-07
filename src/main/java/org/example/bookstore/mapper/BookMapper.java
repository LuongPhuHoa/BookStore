package org.example.bookstore.mapper;

import org.example.bookstore.dto.BookRequestDto;
import org.example.bookstore.dto.BookResponseDto;
import org.example.bookstore.entity.Book;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface BookMapper {
    Book toEntity(BookRequestDto dto);
    BookResponseDto toResponseDto(Book entity);
}
