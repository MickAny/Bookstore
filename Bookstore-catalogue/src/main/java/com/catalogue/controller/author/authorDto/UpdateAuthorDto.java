package com.catalogue.controller.author.authorDto;

import jakarta.validation.constraints.NotNull;

public record UpdateAuthorDto(
        @NotNull
        String name,
        @NotNull
        String details,
        @NotNull
        String image
) {
}
