package com.example.bookstore_interactive.dto.comment;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AddCommentDto {

    @NotBlank(message = "Текст комментария не может быть пустым")
    @Size(min = 1, max = 1000, message = "Комментарий должен содержать от 1 до 1000 символов")
    private String content;

    @NotBlank(message = "Slug песни обязателен")
    private String songSlug;

    @NotBlank(message = "Username пользователя обязателен")
    private String username;
}