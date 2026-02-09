package com.example.bookstore_interactive.services.interfaces;

import com.example.bookstore_interactive.dto.rating.AddRatingDto;
import com.example.bookstore_interactive.dto.rating.RatingDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface SongRatingService {

    // Добавление/обновление оценки песни
    RatingDto addOrUpdateRating(AddRatingDto ratingDto, String userId);

    // Получение оценки пользователя для конкретной песни
    RatingDto getUserRatingForSong(String songId, String userId);

    // Получение всех оценок пользователя
    Page<RatingDto> getUserRatings(String userId, Pageable pageable);

    // Получение средней оценки песни
    Double getAverageRatingForSong(String songId);

    // Проверка, оценивал ли пользователь песню
    boolean hasUserRatedSong(String songId, String userId);

    // Получение всех оценок для песни
    Page<RatingDto> getRatingsForSong(String songId, Pageable pageable);

    // Удаление оценки
    void deleteRating(String songId, String userId);

    // Получение количества оценок для песни
    Long getRatingCountForSong(String songId);

    // Получение статистики оценок (распределение по звездам)
    List<Integer> getRatingDistribution(String songId);
}