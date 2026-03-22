package com.example.bookstore_interactive.repositories;

import com.example.bookstore_interactive.models.entities.SongComment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface SongCommentRepository extends JpaRepository<SongComment, String> {

    @Modifying
    @Transactional
    void deleteById(String commentId);
    Optional<SongComment> findById(String commentId);

    @Query("SELECT c FROM SongComment c JOIN FETCH c.user u WHERE c.song.id = :songId ORDER BY c.createdAt DESC")
    List<SongComment> findBySongIdWithUser(@Param("songId") String songId);

    @Query("SELECT COUNT(sc) FROM SongComment sc")
    Long getTotalCommentsCount();

    @Query("SELECT DISTINCT sc FROM SongComment sc " +
            "LEFT JOIN FETCH sc.song " +
            "LEFT JOIN FETCH sc.user " +
            "ORDER BY sc.createdAt DESC")
    Page<SongComment> getRecentComments(Pageable pageable);
}