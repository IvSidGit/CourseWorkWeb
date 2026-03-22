package com.example.bookstore_interactive.models.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "songs")
public class Song extends BaseEntity {

    @Column(nullable = false, length = 255)
    private String title;

    @Column(nullable = false, length = 100, unique = true)
    private String slug;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "artist_id", nullable = true)
    private Artist artist;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String content;

    @Column(name = "chords_list")
    private String[] chordsList;

    @Column(nullable = false, length = 100)
    private String genre;

    @Column(name = "capo", columnDefinition = "INT CHECK (capo <= 12)")
    private Integer capo = 0;

    @Column(name = "views_count", nullable = false)
    private Integer viewsCount = 0;

    @Column(name = "rating_total", nullable = false)
    private Integer ratingTotal = 0;

    @Column(name = "rating_count", nullable = false)
    private Integer ratingCount = 0;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "created_by")
    private User createdBy;

    // Комментарии к песне
    @OneToMany(mappedBy = "song", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SongComment> comments = new ArrayList<>();

    // Рейтинги песни
    @OneToMany(mappedBy = "song", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SongRating> ratings = new ArrayList<>();

    public Song() {
    }

}