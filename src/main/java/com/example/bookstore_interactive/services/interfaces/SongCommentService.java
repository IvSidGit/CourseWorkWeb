package com.example.bookstore_interactive.services.interfaces;

import com.example.bookstore_interactive.dto.comment.AddCommentDto;
import com.example.bookstore_interactive.dto.comment.CommentDto;

import java.util.List;

public interface SongCommentService {

    // Добавление комментария к песне
    void addComment(AddCommentDto commentDto);

    // Получение комментария по ID
    CommentDto getCommentById(String commentId);

    // Удаление комментария
    void deleteComment(String commentId);

    Long getTotalCommentsCount();

    List<CommentDto> getRecentComments(int limit);
}