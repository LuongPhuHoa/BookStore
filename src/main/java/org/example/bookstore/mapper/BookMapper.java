package org.example.bookstore.mapper;

import org.example.bookstore.dto.BookRequestDto;
import org.example.bookstore.dto.BookResponseDto;
import org.example.bookstore.entity.Book;
import org.mapstruct.Mapper;

/** BookMapper - Converts between Book entity and DTOs using MapStruct. */
@Mapper(componentModel = "spring")  // componentModel="spring" → generates @Component with Spring dependency injection
public interface BookMapper {
    
    /** Convert BookRequestDto (REST input) → Book entity (JPA entity). */
    Book toEntity(BookRequestDto dto);
    
    /** Convert Book entity (from DB) → BookResponseDto (REST output). */
    BookResponseDto toResponseDto(Book entity);
}

