package com.manager.controller;

import com.manager.client.author.AuthorsRestClient;
import com.manager.client.book.BooksRestClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping(("catalogue/menu"))
@RequiredArgsConstructor
public class Menu {

    private final AuthorsRestClient authorsRestClient;
    private final BooksRestClient booksRestClient;

    @GetMapping
    public String getAdminMenuPage(@RequestParam(name = "filter", required = false) String filter, Model model){
        model.addAttribute("authors", authorsRestClient.findAllAuthors());
        model.addAttribute("books", booksRestClient.findAllBooks(filter));
        return "/catalogue/menu/admin_menu";
    }
}
