package com.catalogue.controller.book.bookDto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record NewBookDto(
        @NotNull
        @Size(min = 3, max = 10000, message = "{catalogue.books.create.errors.title_size_is_invalid}")
        String title,
        @Size(max = 10000)
        String details,
        int authorId,
        String image) {
}
