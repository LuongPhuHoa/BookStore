package org.example.bookstore.mapper;

import org.example.bookstore.dto.BookRequest;
import org.example.bookstore.dto.BookResponse;
import org.example.bookstore.entity.Book;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Mappings;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.List;

@Mapper(componentModel = "spring")
public interface BookMapper {

    @Mappings({
            @Mapping(target = "authorId", source = "author.id"),
            @Mapping(target = "authorName", source = "author.name")
    })
    BookResponse toResponse(Book book);

    @Mappings({
            @Mapping(target = "id", ignore = true),
            @Mapping(target = "author", ignore = true)
    })
    Book toEntity(BookRequest request);

    List<BookResponse> toResponseList(List<Book> books);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mappings({
            @Mapping(target = "id", ignore = true),
            @Mapping(target = "author", ignore = true)
    })
    void updateEntityFromRequest(BookRequest request, @MappingTarget Book book);
}
