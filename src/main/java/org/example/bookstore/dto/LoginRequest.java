package org.example.bookstore.dto;

import jakarta.validation.constraints.NotBlank;

public record wLoginRequest(
        @NotBlank String username,
        @NotBlank String password
) {
}
