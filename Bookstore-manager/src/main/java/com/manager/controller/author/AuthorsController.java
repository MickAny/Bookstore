package com.manager.controller.author;

import com.manager.client.author.AuthorsRestClient;
import com.manager.client.exception.BadRequestException;
import com.manager.controller.author.authorDto.NewAuthorDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/catalogue/authors")
public class AuthorsController {
    private final AuthorsRestClient authorsRestClient;

    @GetMapping("list")
    public String getAuthorsList(Model model) {
        model.addAttribute("authors", this.authorsRestClient.findAllAuthors());
        return "catalogue/authors/list";
    }

    @GetMapping("create")
    public String getNewAuthorPage() {
        return "catalogue/authors/new_author";
    }

    @PostMapping("create")
    public String createAuthor(NewAuthorDto newAuthorDto, Model model) {
        try {
            this.authorsRestClient.createAuthor(newAuthorDto.name(), newAuthorDto.details(), newAuthorDto.image());
            return "redirect:/catalogue/menu";
        }catch (BadRequestException exception) {
            model.addAttribute("newAuthorDto", newAuthorDto);
            model.addAttribute("errors", exception.getErrors());
            return "/catalogue/authors/new_author";
        }
    }
}
