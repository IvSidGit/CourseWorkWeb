package com.example.bookstore_interactive.dto.song;

import com.example.bookstore_interactive.models.entities.Artist;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class ShowSongInfoDto {
    private String id;
    private String title;
    private String slug;
    private String genre;
    private String[] chordsList;
    private Integer ratingTotal;
    private Integer ratingCount;
    private LocalDateTime createdAt;
    private String artistName;
}