package com.example.bookstore_interactive.services;

import com.example.bookstore_interactive.dto.rating.AddRatingDto;
import com.example.bookstore_interactive.dto.rating.RatingDto;
import com.example.bookstore_interactive.models.entities.Song;
import com.example.bookstore_interactive.models.entities.SongRating;
import com.example.bookstore_interactive.models.entities.User;
import com.example.bookstore_interactive.repositories.SongRatingRepository;
import com.example.bookstore_interactive.repositories.SongRepository;
import com.example.bookstore_interactive.repositories.UserRepository;
import com.example.bookstore_interactive.services.interfaces.SongRatingService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@Transactional
public class SongRatingServiceImpl implements SongRatingService {

    private SongRatingRepository songRatingRepository;
    private SongRepository songRepository;
    private UserRepository userRepository;
    private ModelMapper modelMapper;

    public SongRatingServiceImpl(SongRatingRepository songRatingRepository,
                                 SongRepository songRepository, UserRepository userRepository,
                                  ModelMapper modelMapper) {
        this.songRatingRepository = songRatingRepository;
        this.songRepository = songRepository;
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
    }

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

            int newTotal = song.getRatingTotal() - oldRating + newRating;
            songRepository.updateRatingStats(song.getId(), song.getRatingCount(), newTotal);

        } else {
            SongRating rating = new SongRating();
            rating.setSong(song);
            rating.setUser(user);
            rating.setRating(ratingDto.getRating());
            savedRating = songRatingRepository.save(rating);

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
    public boolean hasUserRatedSong(String songId, String userId) {
        return songRatingRepository.existsBySongIdAndUserId(songId, userId);
    }

    @Override
    public void deleteRating(String songId, String userId) {
        SongRating rating = songRatingRepository.findBySongIdAndUserId(songId, userId)
                .orElseThrow(() -> new RuntimeException("Оценка не найдена"));

        Song song = rating.getSong();

        song.setRatingCount(song.getRatingCount() - 1);
        song.setRatingTotal(song.getRatingTotal() - rating.getRating());
        song.setUpdatedAt(LocalDateTime.now());

        songRepository.save(song);
        songRatingRepository.delete(rating);
    }
}