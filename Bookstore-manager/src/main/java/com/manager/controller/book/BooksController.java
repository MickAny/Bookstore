package com.manager.controller.book;

import com.manager.client.author.AuthorsRestClient;
import com.manager.client.book.BooksRestClient;
import com.manager.client.exception.BadRequestException;
import com.manager.controller.book.bookDto.NewBookDto;
import com.manager.entiry.Author;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;

@Controller
@RequiredArgsConstructor
@RequestMapping("catalogue/books")
public class BooksController {

    private final BooksRestClient booksRestClient;
    private final AuthorsRestClient authorsRestClient;

    @GetMapping("list")
    public String getBooksListPage(Model model, @RequestParam(name = "filter", required = false) String filter) {
        model.addAttribute("books", this.booksRestClient.findAllBooks(filter));
        model.addAttribute("authors", this.authorsRestClient.findAllAuthors());
        model.addAttribute("filter", filter);
        return "catalogue/books/list";
    }

    @GetMapping("create")
    public String getNewBookPage() {
        return "catalogue/books/new_book";
    }

    @PostMapping("create")
    public String createBookPage(NewBookDto newBookDto, Model model) {
            try {
                Author author = getAuthor(newBookDto.authorId());
                this.booksRestClient.createBook(newBookDto.title(), newBookDto.details(), author, newBookDto.image());
                return "redirect:/catalogue/menu";
            }catch (BadRequestException exception) {
                model.addAttribute("newBookDto", newBookDto);
                model.addAttribute("errors", exception.getErrors());
                return "catalogue/books/new_book";
            }
    }

    public Author getAuthor(int authorId) {
        return this.authorsRestClient.findAuthor(authorId)
                .orElseThrow(() -> new NoSuchElementException("Author not found"));
    }

}
