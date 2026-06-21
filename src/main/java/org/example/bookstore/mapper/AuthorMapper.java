package org.example.bookstore.mapper;

import org.example.bookstore.dto.AuthorRequest;
import org.example.bookstore.dto.AuthorResponse;
import org.example.bookstore.entity.Author;
import org.example.bookstore.entity.Book;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.List;

@Mapper(componentModel = "spring")
public interface AuthorMapper {

    @Mapping(target = "bookIds", source = "books")
    AuthorResponse toResponse(Author author);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "books", ignore = true)
    Author toEntity(AuthorRequest request);

    List<AuthorResponse> toResponseList(List<Author> authors);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "books", ignore = true)
    void updateEntityFromRequest(AuthorRequest request, @MappingTarget Author author);

    default List<Long> mapBooksToBookIds(List<Book> books) {
        if (books == null) {
            return List.of();
        }
        return books.stream()
                .map(Book::getId)
                .toList();
    }
}
