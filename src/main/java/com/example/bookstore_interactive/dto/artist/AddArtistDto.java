package com.example.bookstore_interactive.dto.artist;

import com.example.bookstore_interactive.utils.validation.NotFutureYear;
import jakarta.persistence.Column;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;


@Setter
public class AddArtistDto {
    private String name;

    @NotFutureYear(message = "Год начала карьеры не может быть в будущем")
    private Integer careerStartYear;

    private String description;

    @NotBlank(message = "Имя артиста обязательно")
    @Size(min = 2, max = 255, message = "Имя артиста должно быть от 2 до 255 символов")
    public String getName() {
        return name;
    }

    @NotNull(message = "Начало карьеры обязательно")
    @Min(value = 1800, message = "Начало карьеры не может быть раньше 1800")
    public Integer getCareerStartYear() {
        return careerStartYear;
    }

    @NotBlank(message = "Описание обязательно")
    @Size(min = 10, max = 2000, message = "Описание должно быть от 10 до 2000 символов")
    public String getDescription() {
        return description;
    }
}