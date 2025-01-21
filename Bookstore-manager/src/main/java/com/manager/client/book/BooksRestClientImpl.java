package com.manager.client.book;

import com.manager.client.exception.BadRequestException;
import com.manager.controller.book.bookDto.NewBookDto;
import com.manager.controller.book.bookDto.UpdateBookDto;
import com.manager.entiry.Author;
import com.manager.entiry.Book;
import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.http.ProblemDetail;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@RequiredArgsConstructor
public class BooksRestClientImpl implements BooksRestClient {

    private static final ParameterizedTypeReference<List<Book>> PRODUCTS_TYPE_REFERENCE =
            new ParameterizedTypeReference<>() {
            };
    private final RestClient restClient;

    @Override
    public List<Book> findAllBooksByAuthorId(int authorId) {
        return this.restClient
                .get()
                .uri("/catalogue-api/authors/{authorId}/books", authorId)
                .retrieve()
                .body(PRODUCTS_TYPE_REFERENCE);
    }

    @Override
    public List<Book> findAllBooks(String filter) {
        return this.restClient
                .get()
                .uri("/catalogue-api/books?filter={filter}", filter)
                .retrieve()
                .body(PRODUCTS_TYPE_REFERENCE);
    }

    @Override
    public Book createBook(String title, String details, Author author, String image) {
        try {
            return this.restClient
                    .post() // запрос
                    .uri("/catalogue-api/books")
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(new NewBookDto(title, details, author.id(), image))
                    .retrieve()
                    .body(Book.class);
        }catch (HttpClientErrorException exception){
            ProblemDetail problemDetail = exception.getResponseBodyAs(ProblemDetail.class);
            throw new BadRequestException((List<String>) problemDetail.getProperties().get("errors"));
        }
    }

    @Override
    public Optional<Book> findBook(int bookId) {
        try {
            return Optional.ofNullable(this.restClient.get()
                    .uri("/catalogue-api/books/{bookId}", bookId)
                    .retrieve()
                    .body(Book.class));
        } catch (HttpClientErrorException.NotFound exception) {
            return Optional.empty();
        }
    }

    @Override
    public void updateBook(int bookId, String title, String details, Author author, String image) {
        try {
            this.restClient
                    .patch()
                    .uri("/catalogue-api/books/{bookId}", bookId)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(new UpdateBookDto(title, details, author.id(), image))
                    .retrieve()
                    .toBodilessEntity();
        } catch (HttpClientErrorException.BadRequest exception) {
            ProblemDetail problemDetail = exception.getResponseBodyAs(ProblemDetail.class);
            throw new BadRequestException((List<String>) problemDetail.getProperties().get("errors"));
        }
    }

    @Override
    public void deleteBook(int bookId) {
        try {
            this.restClient
                    .delete()
                    .uri("/catalogue-api/books/{bookId}", bookId)
                    .retrieve()
                    .toBodilessEntity();
        } catch (HttpClientErrorException.NotFound exception) {
            throw new NoSuchElementException(exception);
        }
    }
}
