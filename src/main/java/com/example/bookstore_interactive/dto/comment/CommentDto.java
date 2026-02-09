package com.example.bookstore_interactive.dto.comment;

import com.example.bookstore_interactive.models.entities.Song;
import com.example.bookstore_interactive.models.entities.User;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Getter
@Setter
public class CommentDto {
    private String id;
    private String content;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String authorId;
    private String authorUsername;
    private User user;
    private Song song;

}