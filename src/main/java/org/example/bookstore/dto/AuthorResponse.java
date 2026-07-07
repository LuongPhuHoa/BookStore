package org.example.bookstore.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
public class AuthorResponse {

    private Long id;

    private String name;

    private String nationality;

    private List<Long> bookIds = new ArrayList<>();

}
