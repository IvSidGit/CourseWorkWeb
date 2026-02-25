package com.example.bookstore_interactive.repositories;

import com.example.bookstore_interactive.models.entities.Artist;
import com.example.bookstore_interactive.models.entities.Song;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface ArtistRepository extends JpaRepository<Artist, String> {
    // Поиск по slug
    Optional<Artist> findBySlug(String slug);

    // Проверка существования по slug
    boolean existsBySlug(String slug);

    @Query("SELECT COUNT(a) FROM Artist a")
    Long getTotalArtistsCount();

    @Modifying
    @Transactional
    void deleteById(String artistId);

    List<Artist> findByNameContainingIgnoreCase(String name);
}