package com.manager.controller.author;

import com.manager.client.author.AuthorsRestClient;
import com.manager.client.book.BooksRestClient;
import com.manager.client.exception.BadRequestException;
import com.manager.controller.author.authorDto.UpdateAuthorDto;
import com.manager.entiry.Author;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.NoSuchElementException;

@Controller
@RequestMapping(("catalogue/authors/{authorId:\\d+}"))
@RequiredArgsConstructor
public class AuthorController{
    private final AuthorsRestClient authorsRestClient;
    private final BooksRestClient booksRestClient;

    @ModelAttribute("author")
    public Author author(@PathVariable("authorId") int authorId) {
        return this.authorsRestClient.findAuthor(authorId)
                .orElseThrow(() -> new NoSuchElementException("catalogue.errors.object.not_found"));
    }

    @GetMapping()
    public String getAuthor(@PathVariable("authorId") int authorId, Model model){
        model.addAttribute("books", this.booksRestClient.findAllBooksByAuthorId(authorId));
        return "catalogue/authors/author";
    }

    @GetMapping("edit")
    public String getAuthorEditPage(){
        return "catalogue/authors/edit";
    }

    @PostMapping("edit")
    public String editAuthor(@ModelAttribute(value = "author", binding = false) Author author,
                             UpdateAuthorDto updateAuthorDto, Model model) {

        try{
            this.authorsRestClient.updateAuthor(author.id(), updateAuthorDto.name(), updateAuthorDto.details(), updateAuthorDto.image());
            return "redirect:/catalogue/menu";
        }catch (BadRequestException exception) {
                model.addAttribute("updateAuthorDto", updateAuthorDto);
                model.addAttribute("errors", exception.getErrors());
            return "/catalogue/authors/edit";
        }
    }

    @PostMapping("delete")
    public String deleteAuthor(@PathVariable("authorId") int authorId) {
        this.authorsRestClient.deleteAuthor(authorId);
        return "redirect:/catalogue/menu";
    }

}
