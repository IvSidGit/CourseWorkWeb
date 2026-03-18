package com.example.bookstore_interactive.services;

import com.example.bookstore_interactive.dto.song.AddSongDto;
import com.example.bookstore_interactive.dto.song.ShowDetailedSongInfoDto;
import com.example.bookstore_interactive.dto.song.ShowSongInfoDto;
import com.example.bookstore_interactive.models.entities.Artist;
import com.example.bookstore_interactive.models.entities.Song;
import com.example.bookstore_interactive.models.entities.User;
import com.example.bookstore_interactive.models.exceptions.SongNotFoundException;
import com.example.bookstore_interactive.repositories.ArtistRepository;
import com.example.bookstore_interactive.repositories.SongCommentRepository;
import com.example.bookstore_interactive.repositories.SongRepository;
import com.example.bookstore_interactive.repositories.UserRepository;
import com.example.bookstore_interactive.services.interfaces.SongService;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Slf4j
@Service
@Transactional(readOnly = true)
public class SongServiceImpl implements SongService {

    private final SongRepository songRepository;
    private final ArtistRepository artistRepository;
    private final UserRepository userRepository;
    private final SongCommentRepository songCommentRepository;
    private final ModelMapper mapper;

    public SongServiceImpl(SongRepository songRepository,
                           ArtistRepository artistRepository,
                           UserRepository userRepository, SongCommentRepository songCommentRepository,
                           ModelMapper mapper) {
        this.songRepository = songRepository;
        this.artistRepository = artistRepository;
        this.userRepository = userRepository;
        this.songCommentRepository = songCommentRepository;
        this.mapper = mapper;
        log.info("SongServiceImpl инициализирован");
    }

    @Override
    @Transactional
    @CacheEvict(cacheNames = "songs", allEntries = true)
    public void addSong(AddSongDto songDto) {
        log.debug("Добавление новой песни: {}", songDto.getTitle());

        String baseSlug = Transliteration.transliterateToLatin(songDto.getTitle())
                .toLowerCase()
                .replaceAll("[^a-z0-9]+", "-")
                .replaceAll("^-|-$", "");

        if (baseSlug.isEmpty()) baseSlug = "song";

        String uniqueSlug = baseSlug;
        int counter = 1;

        while (songRepository.existsBySlug(uniqueSlug)) {
            uniqueSlug = baseSlug + "-" + counter;
            counter++;
        }

        Song song = new Song();
        song.setTitle(songDto.getTitle());
        song.setContent(songDto.getContent());
        song.setGenre(songDto.getGenre());
        song.setSlug(uniqueSlug);
        song.setCapo(songDto.getCapo() != null ? songDto.getCapo() : 0);

        if (songDto.getChordsList() != null && !songDto.getChordsList().isEmpty()) {
            song.setChordsList(songDto.getChordsList().toArray(new String[0]));
        }

        if (songDto.getArtistId() != null && !songDto.getArtistId().isEmpty()) {
            Artist artist = artistRepository.findById(songDto.getArtistId())
                    .orElseThrow(() -> new RuntimeException("Артист с ID '" + songDto.getArtistId() + "' не найден"));
            song.setArtist(artist);
        }

        if (songDto.getUserId() != null && !songDto.getUserId().isEmpty()) {
            User user = userRepository.findById(songDto.getUserId())
                    .orElseThrow(() -> new RuntimeException("Пользователь с ID '" + songDto.getUserId() + "' не найден"));
            song.setCreatedBy(user);
        }

        songRepository.save(song);
        log.info("Песня успешно добавлена: {} с названием: {}", songDto.getTitle(), uniqueSlug);
    }

    @Override
    @Cacheable(value = "songs", key = "'all'")
    public List<ShowSongInfoDto> allSongs() {
        log.debug("Получение списка всех песен");
        List<ShowSongInfoDto> songs = songRepository.findAll().stream()
                .map(song -> mapper.map(song, ShowSongInfoDto.class))
                .collect(Collectors.toList());
        log.debug("Найдено песен: {}", songs.size());
        return songs;
    }


    @Transactional
    public ShowDetailedSongInfoDto songInfo(String slug) {
        log.debug("Получение информации о песне: {}", slug);
        Optional<Song> song = songRepository.findBySlug(slug);

        if (song.isEmpty()) {
            log.warn("Песня не найдена: {}", slug);
            throw new SongNotFoundException("Песня '" + slug + "' не найдена");
        }

        String songId = song.get().getId();
        songRepository.incrementViewsCount(songId);

        log.debug("Счетчик просмотров увеличен для песни: {}", slug);

        Song updatedSong = songRepository.findById(songId)
                .orElseThrow(() -> new SongNotFoundException("Песня с ID '" + songId + "' не найдена"));

        ShowDetailedSongInfoDto showDetailedSongInfoDto = mapper.map(updatedSong, ShowDetailedSongInfoDto.class);
        showDetailedSongInfoDto.setComments(songCommentRepository.findBySongIdWithUser(songId));
        return showDetailedSongInfoDto;
    }

    @Override
    @Transactional
    @CacheEvict(cacheNames = "songs", allEntries = true)
    public void deleteSong(String songId) {
        log.debug("Удаление песни: {}", songId);

        Optional<Song> song = songRepository.findById(songId);
        if (song == null) {
            log.warn("Попытка удалить несуществующую песню: {}", songId);
            throw new SongNotFoundException("Песню с ID '" + songId + "' не найдена");
        }

        songRepository.deleteById(songId);
        log.info("Песня удалена: {}", songId);
    }

    @Override
    public String getRandomSlug() {
        Song song = songRepository.findRandomSong()
                .orElseThrow(() -> new SongNotFoundException("Не получилось найти случайную песню"));
        return song.getSlug();
    }

    @Override
    public Long getTotalSongsCount() {
        return songRepository.count();
    }

    @Override
    public Long getTotalViewsCount() {
        return songRepository.sumViewsCount();
    }

    @Override
    public Double getAverageRating() {
        return songRepository.getAverageRating();
    }

    @Override
    public List<ShowDetailedSongInfoDto> getTopPopularSongs(int limit) {
        return songRepository.getTopPopularSongs(5)
                .stream()
                .map(song -> mapper.map(song, ShowDetailedSongInfoDto.class))
                .toList();
    }

    @Override
    public List<ShowDetailedSongInfoDto> getRecentSongs(int limit) {
        return  songRepository.getRecentSongs(PageRequest.of(0, limit))
                .stream()
                .map(song -> mapper.map(song, ShowDetailedSongInfoDto.class))
                .toList();
    }
}