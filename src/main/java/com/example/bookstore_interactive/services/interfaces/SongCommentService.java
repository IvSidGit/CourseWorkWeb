package com.example.bookstore_interactive.services.interfaces;

import com.example.bookstore_interactive.dto.comment.AddCommentDto;
import com.example.bookstore_interactive.dto.comment.CommentDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface SongCommentService {

    // Добавление комментария к песне
    void addComment(AddCommentDto commentDto);
    void addCommentBySlug(String songSlug, String content, String username);


    // Получение комментариев к песне с пагинацией
    List<CommentDto> getCommentsForSong(String songId);

    // Получение комментариев пользователя
    Page<CommentDto> getUserComments(String userId, Pageable pageable);

    // Получение комментария по ID
    CommentDto getCommentById(String commentId);

    // Обновление комментария
    CommentDto updateComment(String commentId, String content, String userId);

    // Удаление комментария
    void deleteComment(String commentId);

    // Получение количества комментариев к песне
    Long getCommentCountForSong(String songId);

    // Получение количества комментариев пользователя
    Long getCommentCountForUser(String userId);

    // Получение последних комментариев (для админ-панели)
    Page<CommentDto> getRecentComments(Pageable pageable);

    Long getTotalCommentsCount();
    List<CommentDto> getRecentComments(int limit);
}