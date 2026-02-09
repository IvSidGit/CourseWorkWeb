package com.example.bookstore_interactive.dto.song;

import com.example.bookstore_interactive.models.entities.Artist;
import com.example.bookstore_interactive.models.entities.Song;
import com.example.bookstore_interactive.models.entities.SongComment;
import com.example.bookstore_interactive.models.entities.User;
import jakarta.persistence.Column;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class ShowDetailedSongInfoDto {
    private String id;
    private String title;
    private String slug;
    private Artist artist;
    private String content;
    private String[] chordsList;
    private String genre;
    private Integer capo;
    private Integer viewsCount;
    private Integer ratingTotal;
    private Integer ratingCount;
    private LocalDateTime createdAt;
    private User createdBy;
    private List<SongComment> comments;
}