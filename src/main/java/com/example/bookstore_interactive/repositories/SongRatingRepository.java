package com.example.bookstore_interactive.repositories;

import com.example.bookstore_interactive.models.entities.SongRating;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SongRatingRepository extends JpaRepository<SongRating, String> {

    // Проверка, оценивал ли пользователь песню
    boolean existsBySongIdAndUserId(String songId, String userId);

    // Получение оценки пользователя для песни
    Optional<SongRating> findBySongIdAndUserId(String songId, String userId);

    // Получение средней оценки песни
    @Query("SELECT COALESCE(AVG(sr.rating), 0) FROM SongRating sr WHERE sr.song.id = :songId")
    Double getAverageRatingBySongId(@Param("songId") String songId);

    // Количество оценок для песни
    Long countBySongId(String songId);

    // Все оценки для песни
    List<SongRating> findBySongId(String songId);
}