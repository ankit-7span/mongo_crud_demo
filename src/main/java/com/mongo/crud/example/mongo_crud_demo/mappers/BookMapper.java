package com.mongo.crud.example.mongo_crud_demo.mappers;

import com.mongo.crud.example.mongo_crud_demo.models.Book;
import com.mongo.crud.example.mongo_crud_demo.request.BookRequest;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface BookMapper {

    @Mapping(target = "name", source = "bookRequest.name")
    @Mapping(target = "quantity", source = "bookRequest.quantity", qualifiedByName = "stringToInteger")
    @Mapping(target = "description", source = "bookRequest.description")
    @Mapping(target = "price", source = "bookRequest.price")
    @Mapping(target = "id", ignore = true)
    Book update(@MappingTarget Book book, BookRequest bookRequest);

    Book toEnity(BookRequest bookRequest);

    @Named("stringToInteger")
    default Integer stringToInteger(String input) {
        if (input != null) {
            return Integer.valueOf(input);
        }
        return null;
    }
}
