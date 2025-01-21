package com.manager;

import com.manager.client.author.AuthorsRestClient;
import com.manager.client.exception.BadRequestException;
import com.manager.controller.author.AuthorsController;
import com.manager.controller.author.authorDto.NewAuthorDto;
import com.manager.entiry.Author;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ui.ConcurrentModel;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Модульные тесты AuthorsController")
public class AuthorControllerTest {

    @Mock
    AuthorsRestClient authorsRestClient;

    @InjectMocks
    AuthorsController controller;

    @Test
    @DisplayName("createAuthor создаст нового автора и перенаправит на страницу админа")
    void createAuthor_RequestIsValid_ReturnsRedirectionToMenuPage() {


        var newAuthorDto = new NewAuthorDto("author1name", "author1details", "author1image");
        var model = new ConcurrentModel();

        doReturn(new Author(1, "author1name", "author1details", "author1image"))
                .when(this.authorsRestClient)
                .createAuthor("author1name", "author1details", "author1image");


        var result = this.controller.createAuthor(newAuthorDto, model);


        assertEquals("redirect:/catalogue/menu", result);
        verify(this.authorsRestClient).createAuthor("author1name", "author1details", "author1image");
        verifyNoMoreInteractions(this.authorsRestClient);

    }

    @Test
    @DisplayName("createAuthor вернёт страницу с ошибками, если запрос невалиден")
    void createAuthor_RequestIsInvalid_ReturnsAuthorFormWithErrors() {

        var newAuthorDto = new NewAuthorDto(" ", null, null);
        var model = new ConcurrentModel();

        doThrow(new BadRequestException(List.of("Error1", "Error2")))
                .when(this.authorsRestClient)
                .createAuthor(" ", null, null);


        var result = this.controller.createAuthor(newAuthorDto, model);


        assertEquals("/catalogue/authors/new_author", result);
        assertEquals(newAuthorDto, model.getAttribute("newAuthorDto"));
        assertEquals(List.of("Error1", "Error2"), model.getAttribute("errors"));

        verify(this.authorsRestClient).createAuthor(" ", null, null);
        verifyNoMoreInteractions(this.authorsRestClient);

    }
}
