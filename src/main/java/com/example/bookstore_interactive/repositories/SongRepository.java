package com.example.bookstore_interactive.repositories;

import com.example.bookstore_interactive.models.entities.Song;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SongRepository extends JpaRepository<Song, String> {

    // Проверка существования песни по slug
    boolean existsBySlug(String slug);

    // Поиск по slug
    Optional<Song> findBySlug(String slug);

    @Modifying
    @org.springframework.transaction.annotation.Transactional
    void deleteById(String songId);

    long count();

    @Query("SELECT SUM(s.viewsCount) FROM Song s")
    Long sumViewsCount();

    @Query("SELECT COALESCE(ROUND(CAST(SUM(s.ratingTotal) AS DOUBLE) / NULLIF(SUM(s.ratingCount), 0), 2), 0.0) FROM Song s WHERE s.ratingCount > 0")
    Double getAverageRating();

    @Query(value = """
    SELECT s.*
    FROM songs s
    WHERE
        s.rating_count > 0
        AND s.rating_total::DECIMAL / s.rating_count >= 4.0
        AND s.views_count > (SELECT AVG(views_count) FROM songs)
    ORDER BY
        s.rating_total::DECIMAL / s.rating_count DESC,
        s.views_count DESC
    LIMIT :limit
    """, nativeQuery = true)
    List<Song> getTopPopularSongs(@Param("limit") int limit);

    @Query("SELECT s FROM Song s ORDER BY s.createdAt DESC")
    Page<Song> getRecentSongs(Pageable pageable);

    @Query(value = "SELECT * FROM songs ORDER BY RANDOM() LIMIT 1", nativeQuery = true)
    Optional<Song> findRandomSong();

    @Modifying
    @Query("UPDATE Song s SET s.ratingCount = :count, s.ratingTotal = :total WHERE s.id = :id")
    void updateRatingStats(@Param("id") String id,
                           @Param("count") Integer count,
                           @Param("total") Integer total);

    @Modifying
    @Transactional
    @Query("UPDATE Song s SET s.viewsCount = s.viewsCount + 1 WHERE s.id = :songId")
    int incrementViewsCount(@Param("songId") String songId);


    List<Song> findByTitleContainingIgnoreCase(String title);

}