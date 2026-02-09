package com.example.bookstore_interactive.dto.artist;

import com.example.bookstore_interactive.models.entities.Song;
import com.example.bookstore_interactive.utils.validation.NotFutureYear;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ShowDetailedArtistInfoDto {
    private String name;
    private String slug;
    private Integer careerStartYear;
    private String description;
    private List<Song> songs;
}