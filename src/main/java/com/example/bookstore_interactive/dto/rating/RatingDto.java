package com.example.bookstore_interactive.dto.rating;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
public class RatingDto {
    private String songId;
    private Integer rating;
}