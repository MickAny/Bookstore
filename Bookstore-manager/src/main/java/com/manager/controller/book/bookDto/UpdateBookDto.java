package com.manager.controller.book.bookDto;

import com.manager.entiry.Author;

public record UpdateBookDto(
        String title,
        String details,
        int authorId,
        String image) {
}
