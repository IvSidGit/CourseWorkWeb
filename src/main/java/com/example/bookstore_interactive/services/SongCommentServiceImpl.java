package com.example.bookstore_interactive.services;

import com.example.bookstore_interactive.dto.artist.AddArtistDto;
import com.example.bookstore_interactive.dto.comment.AddCommentDto;
import com.example.bookstore_interactive.dto.comment.CommentDto;
import com.example.bookstore_interactive.models.entities.Artist;
import com.example.bookstore_interactive.models.entities.Song;
import com.example.bookstore_interactive.models.entities.SongComment;
import com.example.bookstore_interactive.models.entities.User;
import com.example.bookstore_interactive.models.exceptions.SongCommentNotFoundException;
import com.example.bookstore_interactive.models.exceptions.SongNotFoundException;
import com.example.bookstore_interactive.models.exceptions.UserNotFoundException;
import com.example.bookstore_interactive.repositories.SongCommentRepository;
import com.example.bookstore_interactive.repositories.SongRepository;
import com.example.bookstore_interactive.repositories.UserRepository;
import com.example.bookstore_interactive.services.interfaces.SongCommentService;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@Transactional(readOnly = true)
public class SongCommentServiceImpl implements SongCommentService {
    SongCommentRepository songCommentRepository;
    SongRepository songRepository;
    UserRepository userRepository;
    ModelMapper mapper;

    public SongCommentServiceImpl(SongCommentRepository songCommentRepository,
                                  SongRepository songRepository, UserRepository userRepository,
                                  ModelMapper mapper) {
        this.songCommentRepository = songCommentRepository;
        this.songRepository = songRepository;
        this.userRepository = userRepository;
        this.mapper = mapper;
    }

    @Override
    @Transactional
    @CacheEvict(cacheNames = "song-comments", allEntries = true)
    public void addComment(AddCommentDto commentDTO) {
        log.debug("Добавление нового комментария");
        log.debug("Полученный AddCommentDto: {}", commentDTO);
        log.debug("Content из DTO: {}", commentDTO.getContent()); // ← Добавьте эту строку!

        // Проверка, что content не null
        if (commentDTO.getContent() == null || commentDTO.getContent().trim().isEmpty()) {
            log.error("Content is null or empty in AddCommentDto: {}", commentDTO);
            throw new IllegalArgumentException("Текст комментария не может быть пустым");
        }

        // Создаем комментарий
        SongComment comment = new SongComment();
        comment.setContent(commentDTO.getContent()); // ← ВАЖНО: устанавливаем content!
        comment.setCreatedAt(LocalDateTime.now());
        comment.setUpdatedAt(LocalDateTime.now());

        // Находим песню
        Song song = songRepository.findBySlug(commentDTO.getSongSlug())
                .orElseThrow(() -> new SongNotFoundException("Песня с slug '" + commentDTO.getSongSlug() + "' не найдена"));
        comment.setSong(song);

        // Находим пользователя
        User user = userRepository.findByUsername(commentDTO.getUsername())
                .orElseThrow(() -> new UserNotFoundException("Пользователь с username '" + commentDTO.getUsername() + "' не найден"));
        comment.setUser(user);

        // Сохраняем
        songCommentRepository.save(comment);
        log.info("Комментарий успешно добавлен для песни: {}, пользователем: {}",
                commentDTO.getSongSlug(), commentDTO.getUsername());
    }

    public void addCommentBySlug(String songSlug, String content, String username) {
        // Находим песню по slug
        Song song = songRepository.findBySlug(songSlug)
                .orElseThrow(() -> new SongNotFoundException("Песня не найдена"));

        // Находим пользователя по username
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("Пользователь не найден"));

        // Создаем комментарий
        SongComment comment = new SongComment();
        comment.setContent(content);
        comment.setSong(song);
        comment.setUser(user);

        songCommentRepository.save(comment);
    }

    public List<CommentDto> getCommentsForSong(String songId) {
        List<SongComment> comments = songCommentRepository.findBySongIdWithUser(songId);

        return comments.stream()
                .map(comment -> mapper.map(comment,CommentDto.class))
                .toList();
    }

    @Override
    public Page<CommentDto> getUserComments(String userId, Pageable pageable) {
        return null;
    }

    @Override
    public CommentDto getCommentById(String commentId) {
        return songCommentRepository.findById(commentId)
                .map(comment -> mapper.map(comment, CommentDto.class))
                .orElseThrow(() -> new SongCommentNotFoundException("Комментарий с ID '" + commentId + "' не найден"));
    }

    @Override
    public CommentDto updateComment(String commentId, String content, String userId) {
        return null;
    }

    @Override
    @Transactional
    @CacheEvict(cacheNames = "songComments", allEntries = true)
    public void deleteComment(String commentId) {
        log.debug("Удаление комментария: {}", commentId);

        Optional<SongComment> comment = songCommentRepository.findById(commentId);
        if (comment == null) {
            log.warn("Попытка удалить несуществующий комментарий: {}", commentId);
            throw new SongCommentNotFoundException("Комментарий с ID '" + commentId + "' не найден");
        }

        songCommentRepository.deleteById(commentId);
        log.info("Комментарий удален: {}", commentId);
    }

    @Override
    public Long getCommentCountForSong(String songId) {
        return 0L;
    }

    @Override
    public Long getCommentCountForUser(String userId) {
        return 0L;
    }

    @Override
    public Page<CommentDto> getRecentComments(Pageable pageable) {
        return null;
    }

    @Override
    public Long getTotalCommentsCount() {
        return songCommentRepository.getTotalCommentsCount();
    }

    @Override
    public List<CommentDto> getRecentComments(int limit) {
        return songCommentRepository.getRecentComments(PageRequest.of(0, limit))
                .stream().map(songComment -> mapper.map(songComment, CommentDto.class)).toList();
    }
}