package com.example.bookstore_interactive.models.exceptions;

public class SongCommentNotFoundException extends RuntimeException {

  public SongCommentNotFoundException(String message) {
    super(message);
  }

  public SongCommentNotFoundException(String message, Throwable cause) {
    super(message, cause);
  }
}

