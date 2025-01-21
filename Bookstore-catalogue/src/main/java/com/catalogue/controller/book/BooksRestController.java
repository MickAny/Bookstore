package com.catalogue.controller.book;

import com.catalogue.controller.book.bookDto.NewBookDto;
import com.catalogue.entity.Author;
import com.catalogue.entity.Book;
import com.catalogue.service.author.AuthorService;
import com.catalogue.service.book.BookService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Map;
import java.util.NoSuchElementException;

@RestController
@RequiredArgsConstructor
@RequestMapping("catalogue-api/books")
public class BooksRestController {

    private final BookService bookService;
    private final AuthorService authorService;

    @GetMapping
    public Iterable<Book> findBooks(@RequestParam(name = "filter", required = false) String filter) {
        return this.bookService.findAllBooks(filter);
    }

    /**
     * @param uriComponentsBuilder формирует ссылку на созданный объект для последующей её передачи в метод created()
     */
    @PostMapping
    public ResponseEntity<?> createBook(@Valid @RequestBody NewBookDto newBookDto,
                                        BindingResult bindingResult, UriComponentsBuilder uriComponentsBuilder) throws BindException {
        if(bindingResult.hasErrors()) {
            if(bindingResult instanceof BindException exception) {
                throw exception;
            }else{
                throw new BindException(bindingResult);
            }
        }else{
            Author author = getAuthor(newBookDto.authorId());
            Book newBook = this.bookService.createBook(newBookDto.title(), newBookDto.details(), author, newBookDto.image());
            return ResponseEntity
                    .created(uriComponentsBuilder
                            .replacePath("/catalogue-api/books/{bookId}")
                            .build(Map.of("bookId", newBook.getId())))
                    .body(newBook);

        }

    }

    public Author getAuthor(int authorId) {
        return this.authorService.findAuthor(authorId)
                .orElseThrow(() -> new NoSuchElementException("Author not found"));
    }
}
