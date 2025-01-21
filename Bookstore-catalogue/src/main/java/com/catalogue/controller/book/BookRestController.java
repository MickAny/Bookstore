package com.catalogue.controller.book;

import com.catalogue.controller.book.bookDto.UpdateBookDto;
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
@RequestMapping("catalogue-api/books/{bookId:\\d+}")
public class BookRestController {

    private final BookService bookService;
    private final AuthorService authorService;

    private final MessageSource messageSource;

    @ModelAttribute("book")
    public Book getBook(@PathVariable("bookId") int bookId) {
        return this.bookService.findBook(bookId)
                .orElseThrow(() -> new NoSuchElementException("catalogue.errors.book.not_found"));
    }

    @GetMapping
    public Book findBook(@ModelAttribute("book") Book book) {
        return book;
    }

    @PatchMapping
    public ResponseEntity<?> updateBook(@PathVariable("bookId") int bookId,
                                        @Valid @RequestBody UpdateBookDto updateBookDto,
                                        BindingResult bindingResult) throws BindException {
        if (bindingResult.hasErrors()) {
            if(bindingResult instanceof BindException exception) {
                throw exception;
            }else{
                throw new BindException(bindingResult);
            }
        }else{
            Author author = getAuthor(updateBookDto.authorId());
            this.bookService.updateBook(bookId, updateBookDto.title(), updateBookDto.details(), author, updateBookDto.image());
            return ResponseEntity.noContent().build();
        }
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteBook(@PathVariable("bookId") int bookId) {
        this.bookService.deleteBook(bookId);
        return ResponseEntity.noContent().build();
    }

    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<ProblemDetail> handleNoSuchElementException(NoSuchElementException exception, Locale locale) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND,
                                this.messageSource.getMessage(exception.getMessage(), null, exception.getMessage(), locale)));
    }


    public Author getAuthor(int authorId) {
        return this.authorService.findAuthor(authorId)
                .orElseThrow(() -> new NoSuchElementException("Author not found"));
    }

}
