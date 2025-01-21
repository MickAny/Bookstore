package com.catalogue.controller.author;

import com.catalogue.controller.author.authorDto.UpdateAuthorDto;
import com.catalogue.entity.Author;
import com.catalogue.entity.Book;
import com.catalogue.service.author.AuthorService;
import com.catalogue.service.book.BookService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.Locale;
import java.util.NoSuchElementException;

@RestController
@RequiredArgsConstructor
@RequestMapping("catalogue-api/authors/{authorId:\\d+}")
public class AuthorRestController {

    private final BookService bookService;
    private final AuthorService authorService;
    private final MessageSource messageSource;

    @ModelAttribute("author")
    public Author getAuthor(@PathVariable("authorId") int authorId) {
        return this.authorService.findAuthor(authorId)
                .orElseThrow(() -> new NoSuchElementException("Author not found"));
    }

    @GetMapping("books")
    public Iterable<Book> findBooksByAuthorId(@PathVariable("authorId") int authorId) {
        return this.bookService.findAllBooksByAuthorId(authorId);
    }

    @GetMapping
    public Author findAuthor(@ModelAttribute("author") Author author) {
        return author;
    }

    @PatchMapping
    public ResponseEntity<?> updateAuthor(@PathVariable("authorId") int authorId,
                                          @Valid @RequestBody UpdateAuthorDto updateAuthorDto,
                                          BindingResult bindingResult) throws BindException {
        if (bindingResult.hasErrors()) {
            if(bindingResult instanceof BindException exception) {
                throw exception;
            }else{
                throw new BindException(bindingResult);
            }
        }else{
            this.authorService.updateAuthor(authorId, updateAuthorDto.name(), updateAuthorDto.details(), updateAuthorDto.image());
            return ResponseEntity.noContent().build();
        }
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteAuthor(@PathVariable("authorId") int authorId) {
        this.authorService.deleteAuthor(authorId);
        return ResponseEntity.noContent().build();
    }

    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<ProblemDetail> handleNoSuchElementException(NoSuchElementException exception, Locale locale) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND,
                        this.messageSource.getMessage(exception.getMessage(), null, exception.getMessage(), locale)));
    }


}
