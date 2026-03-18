package com.example.bookstore_interactive.services.interfaces;

import com.example.bookstore_interactive.dto.rating.AddRatingDto;
import com.example.bookstore_interactive.dto.rating.RatingDto;

public interface SongRatingService {

    // Добавление/обновление оценки песни
    RatingDto addOrUpdateRating(AddRatingDto ratingDto, String userId);

    // Получение оценки пользователя для конкретной песни
    RatingDto getUserRatingForSong(String songId, String userId);

    // Проверка, оценивал ли пользователь песню
    boolean hasUserRatedSong(String songId, String userId);

    // Удаление оценки
    void deleteRating(String songId, String userId);
}