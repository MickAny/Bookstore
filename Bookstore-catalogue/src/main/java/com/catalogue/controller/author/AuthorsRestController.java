package com.catalogue.controller.author;

import com.catalogue.controller.author.authorDto.NewAuthorDto;
import com.catalogue.entity.Author;
import com.catalogue.service.author.AuthorService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("catalogue-api/authors")
public class AuthorsRestController {

    private final AuthorService authorService;

    @GetMapping
    public Iterable<Author> findAuthors() {
        return this.authorService.findAllAuthors();
    }

    /**
     * @param uriComponentsBuilder формирует ссылку на созданный объект для последующей её передачи в метод created()
     */
    @PostMapping
    public ResponseEntity<?> createAuthor(@Valid @RequestBody NewAuthorDto newAuthorDto,
                                           BindingResult bindingResult,
                                          UriComponentsBuilder uriComponentsBuilder) throws BindException {
        if(bindingResult.hasErrors()) {
            if(bindingResult instanceof BindException exception) {
                throw exception;
            }else{
                throw new BindException(bindingResult);
            }
        }else{
            Author newAuthor = this.authorService.createAuthor(newAuthorDto.name(), newAuthorDto.details(), newAuthorDto.image());
            return ResponseEntity
                    .created(uriComponentsBuilder
                            .replacePath("/catalogue-api/authors/{authorId}")
                            .build(Map.of("authorId", newAuthor.getId())))
                    .body(newAuthor);

        }

    }
}
