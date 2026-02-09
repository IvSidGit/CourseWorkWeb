package com.example.bookstore_interactive.models.entities;

import com.example.bookstore_interactive.utils.validation.NotFutureYear;
import com.example.bookstore_interactive.utils.validation.UniqueEmail;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;
import java.time.Year;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "artists")
public class Artist extends BaseEntity {
    @Column(nullable = false, length = 255)
    private String name;

    @Column(name = "career_start_year", nullable = false)
    @Min(1800)
    @NotFutureYear(message = "Год начала карьеры не может быть в будущем")
    private Integer careerStartYear;

    @Column(unique = true, nullable = false, length = 100)
    private String slug;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String description;

    @OneToMany(mappedBy = "artist", cascade = CascadeType.PERSIST)
    private List<Song> songs = new ArrayList<>();

    public Artist() {
    }
}