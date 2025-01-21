package com.catalogue.controller.book.bookDto;

import com.catalogue.entity.Author;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record UpdateBookDto(
        @NotNull(message = "{catalogue.books.create.errors.title_is_null}")
        @Size(min = 3, max = 50, message = "{catalogue.books.update.errors.title_size_is_invalid}")
        String title,
        @Size(max = 500, message = "{catalogue.books.update.errors.details_size_is_invalid}")
        String details,
        int authorId,
        String image) {
}
