package com.manager.client.author;

import com.manager.client.exception.BadRequestException;
import com.manager.controller.author.authorDto.NewAuthorDto;
import com.manager.controller.author.authorDto.UpdateAuthorDto;
import com.manager.entiry.Author;
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
public class AuthorsRestClientImpl implements AuthorsRestClient {

    private static final ParameterizedTypeReference<List<Author>> AUTHORS_TYPE_REFERENCE =
            new ParameterizedTypeReference<>() {};
    private final RestClient restClient;

    @Override
    public List<Author> findAllAuthors() {
        return this.restClient
                .get()
                .uri("/catalogue-api/authors")
                .retrieve()
                .body(AUTHORS_TYPE_REFERENCE);
    }

    @Override
    public Author createAuthor(String name, String details, String image) {
        try{
            return this.restClient
                    .post()
                    .uri("/catalogue-api/authors")
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(new NewAuthorDto(name, details, image))
                    .retrieve()
                    .body(Author.class);
        }catch (HttpClientErrorException exception){
            ProblemDetail problemDetail = exception.getResponseBodyAs(ProblemDetail.class);
            throw new BadRequestException((List<String>) problemDetail.getProperties().get("errors"));
        }
    }

    @Override
    public Optional<Author> findAuthor(int authorId) {
        try {
            return Optional.ofNullable(this.restClient.get()
                    .uri("/catalogue-api/authors/{authorId}", authorId)
                    .retrieve()
                    .body(Author.class));
        } catch (HttpClientErrorException.NotFound exception) {
            return Optional.empty();
        }
    }

    @Override
    public void updateAuthor(int authorId, String name, String details, String image) {
        try {
            this.restClient
                    .patch()
                    .uri("/catalogue-api/authors/{authorId}", authorId)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(new UpdateAuthorDto(name, details, image))
                    .retrieve()
                    .toBodilessEntity();
        } catch (HttpClientErrorException.BadRequest exception) {
            ProblemDetail problemDetail = exception.getResponseBodyAs(ProblemDetail.class);
            throw new BadRequestException((List<String>) problemDetail.getProperties().get("errors"));
        }
    }

    @Override
    public void deleteAuthor(int authorId) {
        try {
            this.restClient
                    .delete()
                    .uri("/catalogue-api/authors/{authorId}", authorId)
                    .retrieve()
                    .toBodilessEntity();
        } catch (HttpClientErrorException.NotFound exception) {
            throw new NoSuchElementException(exception);
        }
    }
}
