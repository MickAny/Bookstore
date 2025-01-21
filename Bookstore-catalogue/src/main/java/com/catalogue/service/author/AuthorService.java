package com.catalogue.service.author;

import com.catalogue.entity.Author;
import com.catalogue.entity.Book;

import java.util.Optional;

public interface AuthorService {
    Iterable<Author> findAllAuthors();

    Author createAuthor(String name, String details, String image);

    Optional<Author> findAuthor(int AuthorId);

    void updateAuthor(Integer id, String name, String details, String image);

    void deleteAuthor(Integer id);
}
