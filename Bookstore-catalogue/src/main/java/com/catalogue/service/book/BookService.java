package com.catalogue.service.book;


import com.catalogue.entity.Author;
import com.catalogue.entity.Book;

import java.util.Optional;

public interface BookService {

    Iterable<Book> findAllBooks(String filter);

    Iterable<Book> findAllBooksByAuthorId(int authorId);

    Book createBook(String title, String details, Author author, String image);

    Optional<Book> findBook(int productId);

    void updateBook(Integer id, String title, String details, Author author, String image);

    void deleteBook(Integer id);
}

