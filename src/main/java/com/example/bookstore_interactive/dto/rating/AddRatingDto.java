package com.example.bookstore_interactive.dto.rating;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AddRatingDto {
    private String songId;
    private Integer rating;
}