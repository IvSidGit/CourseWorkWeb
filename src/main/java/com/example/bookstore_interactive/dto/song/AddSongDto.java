package com.example.bookstore_interactive.dto.song;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class AddSongDto {


    @NotBlank(message = "Название песни обязательно")
    @Size(min = 1, max = 255, message = "Название песни должно быть от 1 до 255 символов")
    private String title;

    @NotBlank(message = "Текст песни обязателен")
    private String content;

    private String artistName;

    private String artistId;
    private String userId;

//    private String slug;

    @Size(max = 100, message = "Жанр не может превышать 100 символов")
    private String genre;

    private List<String> chordsList;

    @Min(value = 0, message = "Капо не может быть меньше 0")
    @Max(value = 12, message = "Капо не может быть больше 12")
    private Integer capo = 0;


}