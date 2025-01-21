package com.manager.controller.book;

import com.manager.client.author.AuthorsRestClient;
import com.manager.client.book.BooksRestClient;
import com.manager.client.exception.BadRequestException;
import com.manager.controller.book.bookDto.UpdateBookDto;
import com.manager.entiry.Author;
import com.manager.entiry.Book;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.NoSuchElementException;

@Controller
@RequestMapping(("catalogue/books/{bookId:\\d+}"))
@RequiredArgsConstructor
public class BookController {

    private final BooksRestClient booksRestClient;
    private final AuthorsRestClient authorsRestClient;

    @ModelAttribute("book")
    public Book getBook(@PathVariable("bookId") int productId) {
        return this.booksRestClient.findBook(productId)
                .orElseThrow(() -> new NoSuchElementException("catalogue.errors.object.not_found"));
    }

    @GetMapping()
    public String getBookPage(){
        return "catalogue/books/book";
    }

    @GetMapping("edit")
    public String getBookEditPage(){
        return "catalogue/books/edit";
    }

    @PostMapping("edit")
    public String editBook(@ModelAttribute(value = "book", binding = false) Book book,
                           UpdateBookDto updateBookDto, Model model) {
            try{
                Author author = getAuthor(updateBookDto.authorId());
                this.booksRestClient.updateBook(book.id(), updateBookDto.title(), updateBookDto.details(), author, updateBookDto.image());
                return "redirect:/catalogue/menu";
            }catch (BadRequestException exception) {
                model.addAttribute("updateBookDto", updateBookDto);
                model.addAttribute("errors", exception.getErrors());
                return "/catalogue/books/edit";
            }
    }

    @PostMapping("delete")
    public String deleteBook(@PathVariable("bookId") int productId) {
        this.booksRestClient.deleteBook(productId);
        return "redirect:/catalogue/menu";
    }

    public Author getAuthor(int authorId) {
        return this.authorsRestClient.findAuthor(authorId)
                .orElseThrow(() -> new NoSuchElementException("Author not found"));
    }
}

