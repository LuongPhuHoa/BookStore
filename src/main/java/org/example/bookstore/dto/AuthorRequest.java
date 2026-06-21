package org.example.bookstore.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class AuthorRequest {

    @NotBlank(message = "Author name is required")
    private String name;

    @NotBlank(message = "Author nationality is required")
    private String nationality;

    public AuthorRequest() {
    }

    public AuthorRequest(String name, String nationality) {
        this.name = name;
        this.nationality = nationality;
    }

}
