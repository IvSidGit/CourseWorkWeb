package com.example.bookstore_interactive.services;

import com.example.bookstore_interactive.dto.comment.AddCommentDto;
import com.example.bookstore_interactive.dto.comment.CommentDto;
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
import org.springframework.data.domain.PageRequest;
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
        log.debug("Content из DTO: {}", commentDTO.getContent());

        if (commentDTO.getContent() == null || commentDTO.getContent().trim().isEmpty()) {
            log.error("Content is null or empty in AddCommentDto: {}", commentDTO);
            throw new IllegalArgumentException("Текст комментария не может быть пустым");
        }

        SongComment comment = new SongComment();
        comment.setContent(commentDTO.getContent());
        comment.setCreatedAt(LocalDateTime.now());
        comment.setUpdatedAt(LocalDateTime.now());

        Song song = songRepository.findBySlug(commentDTO.getSongSlug())
                .orElseThrow(() -> new SongNotFoundException("Песня с slug '" + commentDTO.getSongSlug() + "' не найдена"));
        comment.setSong(song);

        User user = userRepository.findByUsername(commentDTO.getUsername())
                .orElseThrow(() -> new UserNotFoundException("Пользователь с username '" + commentDTO.getUsername() + "' не найден"));
        comment.setUser(user);

        songCommentRepository.save(comment);
        log.info("Комментарий успешно добавлен для песни: {}, пользователем: {}",
                commentDTO.getSongSlug(), commentDTO.getUsername());
    }

    @Override
    public CommentDto getCommentById(String commentId) {
        return songCommentRepository.findById(commentId)
                .map(comment -> mapper.map(comment, CommentDto.class))
                .orElseThrow(() -> new SongCommentNotFoundException("Комментарий с ID '" + commentId + "' не найден"));
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
    public Long getTotalCommentsCount() {
        return songCommentRepository.getTotalCommentsCount();
    }

    @Override
    public List<CommentDto> getRecentComments(int limit) {
        return songCommentRepository.getRecentComments(PageRequest.of(0, limit))
                .stream().map(songComment -> mapper.map(songComment, CommentDto.class)).toList();
    }
}