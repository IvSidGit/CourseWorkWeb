package com.example.bookstore_interactive.dto.artist;

import com.example.bookstore_interactive.utils.validation.NotFutureYear;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ShowArtistInfoDto {
    private String id;
    private String name;
    private String slug;
    private Integer careerStartYear;
}

