package com.catalogue.controller.author.authorDto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record NewAuthorDto(
        @NotNull
        @Size(min = 3, max = 50, message = "Invalid Author Name")
        String name,
        @NotNull
        String details,
        @NotNull
        String image) {
}
