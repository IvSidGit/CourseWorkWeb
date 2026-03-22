package com.example.bookstore_interactive.services.interfaces;

import com.example.bookstore_interactive.dto.comment.AddCommentDto;
import com.example.bookstore_interactive.dto.comment.CommentDto;

import java.util.List;

public interface SongCommentService {

    void addComment(AddCommentDto commentDto);

    CommentDto getCommentById(String commentId);

    void deleteComment(String commentId);

    Long getTotalCommentsCount();

    List<CommentDto> getRecentComments(int limit);
}