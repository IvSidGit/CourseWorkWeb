package com.example.bookstore_interactive.services;

import com.example.bookstore_interactive.dto.rating.AddRatingDto;
import com.example.bookstore_interactive.dto.rating.RatingDto;
import com.example.bookstore_interactive.models.entities.Song;
import com.example.bookstore_interactive.models.entities.SongRating;
import com.example.bookstore_interactive.models.entities.User;
import com.example.bookstore_interactive.models.exceptions.SongNotFoundException;
import com.example.bookstore_interactive.repositories.SongRatingRepository;
import com.example.bookstore_interactive.repositories.SongRepository;
import com.example.bookstore_interactive.repositories.UserRepository;
import com.example.bookstore_interactive.services.interfaces.SongRatingService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class SongRatingServiceImpl implements SongRatingService {

    @Autowired
    private SongRatingRepository songRatingRepository;

    @Autowired
    private SongRepository songRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    @Transactional
    public RatingDto addOrUpdateRating(AddRatingDto ratingDto, String userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Пользователь не найден"));

        Song song = songRepository.findById(ratingDto.getSongId())
                .orElseThrow(() -> new RuntimeException("Песня не найдена"));

        Optional<SongRating> existingRatingOpt = songRatingRepository
                .findBySongIdAndUserId(ratingDto.getSongId(), userId);

        SongRating savedRating;

        if (existingRatingOpt.isPresent()) {
            SongRating existingRating = existingRatingOpt.get();
            int oldRating = existingRating.getRating();
            int newRating = ratingDto.getRating();

            existingRating.setRating(newRating);
            savedRating = songRatingRepository.save(existingRating);

            // Прямое обновление в БД
            int newTotal = song.getRatingTotal() - oldRating + newRating;
            songRepository.updateRatingStats(song.getId(), song.getRatingCount(), newTotal);

        } else {
            SongRating rating = new SongRating();
            rating.setSong(song);
            rating.setUser(user);
            rating.setRating(ratingDto.getRating());
            savedRating = songRatingRepository.save(rating);

            // Прямое обновление в БД
            int newCount = song.getRatingCount() + 1;
            int newTotal = song.getRatingTotal() + ratingDto.getRating();
            songRepository.updateRatingStats(song.getId(), newCount, newTotal);
        }

        return modelMapper.map(savedRating, RatingDto.class);
    }


    @Override
    public RatingDto getUserRatingForSong(String songId, String userId) {
        SongRating rating = songRatingRepository.findBySongIdAndUserId(songId, userId)
                .orElseThrow(() -> new RuntimeException("Оценка не найдена"));

        return modelMapper.map(rating, RatingDto.class);
    }

    @Override
    public Page<RatingDto> getUserRatings(String userId, Pageable pageable) {
        return null;
    }

//    @Override
//    public Page<RatingDto> getUserRatings(String userId, Pageable pageable) {
//        return songRatingRepository.findByUserId(userId, pageable)
//                .map(this::convertToRatingDto);
//    }

    @Override
    public Double getAverageRatingForSong(String songId) {
        Double avg = songRatingRepository.getAverageRatingBySongId(songId);
        return avg != null ? Math.round(avg * 10.0) / 10.0 : 0.0;
    }

    @Override
    public boolean hasUserRatedSong(String songId, String userId) {
        return songRatingRepository.existsBySongIdAndUserId(songId, userId);
    }

    @Override
    public Page<RatingDto> getRatingsForSong(String songId, Pageable pageable) {
        return null;
    }

//    @Override
//    public Page<RatingDto> getRatingsForSong(String songId, Pageable pageable) {
//        return songRatingRepository.findBySongId(songId)
//                .map(this::convertToRatingDto);
//    }

    @Override
    public void deleteRating(String songId, String userId) {
        SongRating rating = songRatingRepository.findBySongIdAndUserId(songId, userId)
                .orElseThrow(() -> new RuntimeException("Оценка не найдена"));

        Song song = rating.getSong();

        // Обновляем статистику песни
        song.setRatingCount(song.getRatingCount() - 1);
        song.setRatingTotal(song.getRatingTotal() - rating.getRating());
        song.setUpdatedAt(LocalDateTime.now());

        songRepository.save(song);
        songRatingRepository.delete(rating);
    }

    @Override
    public Long getRatingCountForSong(String songId) {
        return songRatingRepository.countBySongId(songId);
    }

    @Override
    public List<Integer> getRatingDistribution(String songId) {
        List<SongRating> ratings = songRatingRepository.findBySongId(songId);

        // Инициализируем массив для распределения по звездам (1-5)
        Integer[] distribution = new Integer[5];
        Arrays.fill(distribution, 0);

        for (SongRating rating : ratings) {
            int stars = rating.getRating();
            if (stars >= 1 && stars <= 5) {
                distribution[stars - 1]++;
            }
        }

        return Arrays.asList(distribution);
    }

//    private RatingDto convertToRatingDto(SongRating rating) {
//        RatingDto dto = modelMapper.map(rating, RatingDto.class);
//
//        // Дополнительные поля
//        dto.setSongTitle(rating.getSong().getTitle());
//        dto.setSongSlug(rating.getSong().getSlug());
//        dto.setUsername(rating.getUser().getUsername());
//
//        return dto;
//    }
}