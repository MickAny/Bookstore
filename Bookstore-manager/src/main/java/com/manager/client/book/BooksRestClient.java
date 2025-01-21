package com.manager.client.book;

import com.manager.entiry.Author;
import com.manager.entiry.Book;

import java.util.List;
import java.util.Optional;

public interface BooksRestClient {

    List<Book> findAllBooks(String filter);
    Book createBook(String title, String details, Author author, String image);
    Optional<Book> findBook(int productId);
    void updateBook(int productId, String title, String details, Author author, String image);
    void deleteBook(int productId);
    List<Book> findAllBooksByAuthorId(int authorId);
}
