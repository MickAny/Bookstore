package com.manager.client.author;

import com.manager.entiry.Author;

import java.util.List;
import java.util.Optional;

public interface AuthorsRestClient {
    List<Author> findAllAuthors();
    Author createAuthor(String name, String details, String image);
    Optional<Author> findAuthor(int authorId);
    void updateAuthor(int authorId, String name, String details, String image);
    void deleteAuthor(int authorId);
}
