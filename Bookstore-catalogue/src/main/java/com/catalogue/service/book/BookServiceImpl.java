package com.catalogue.service.book;

import com.catalogue.entity.Author;
import com.catalogue.entity.Book;
import com.catalogue.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;

    @Override
    public Iterable<Book> findAllBooks(String filter) {
        if (filter != null && !filter.isBlank()) {
            return this.bookRepository.findAllByTitleLikeIgnoreCase("%" + filter + "%");
        } else {
            return this.bookRepository.findAll();
        }
    }

    @Override
    public Iterable<Book> findAllBooksByAuthorId(int authorId) {
        return this.bookRepository.findBooksByAuthor_Id(authorId);
    }

    @Override
    @Transactional
    public Book createBook(String title, String details, Author author, String image) {
        return this.bookRepository.save(new Book(null, title, details, author, image));
    }

    @Override
    public Optional<Book> findBook(int productId) {
        return this.bookRepository.findById(productId);
    }

    @Override
    @Transactional
    public void updateBook(Integer id, String title, String details, Author author, String image) {
        this.bookRepository.findById(id)
                .ifPresentOrElse(product -> {
                    product.setTitle(title);
                    product.setDetails(details);
                    product.setAuthor(author);
                    product.setImage(image);

                    this.bookRepository.save(product);
                }, () -> {
                    throw new NoSuchElementException();
                });
    }

    @Override
    @Transactional
    public void deleteBook(Integer id) {
        this.bookRepository.deleteById(id);
    }
}
