package com.manager.controller.exception_handlers;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Locale;
import java.util.NoSuchElementException;

@ControllerAdvice
@RequiredArgsConstructor
public class ExceptionHandlers {

    private final MessageSource messageSource;

    /**
     * Обработчик исключения NoSuchElementException
     * Выкидывает состояние сессии, после прогружает страницу 404 с пояснением
     */
    @ExceptionHandler(NoSuchElementException.class)
    public String handleNoSuchElementException(NoSuchElementException exception, Model model,
                                               HttpServletResponse response, Locale locale) {
        response.setStatus(HttpStatus.NOT_FOUND.value());
        model.addAttribute("error",
                this.messageSource.getMessage(exception.getMessage(), null,
                        exception.getMessage(), locale));
        return "errors/404";
    }
}
