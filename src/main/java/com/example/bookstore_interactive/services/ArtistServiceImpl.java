package com.example.bookstore_interactive.services;

import com.example.bookstore_interactive.dto.artist.*;
import com.example.bookstore_interactive.models.entities.Artist;
import com.example.bookstore_interactive.models.entities.Song;
import com.example.bookstore_interactive.models.entities.SongComment;
import com.example.bookstore_interactive.models.exceptions.ArtistNotFoundException;
import com.example.bookstore_interactive.models.exceptions.SongCommentNotFoundException;
import com.example.bookstore_interactive.repositories.ArtistRepository;
import com.example.bookstore_interactive.repositories.SongRepository;
import com.example.bookstore_interactive.services.interfaces.ArtistService;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Year;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional(readOnly = true)
public class ArtistServiceImpl implements ArtistService {

    private final ArtistRepository artistRepository;
    private final SongRepository songRepository;
    private final ModelMapper mapper;

    public ArtistServiceImpl(ArtistRepository artistRepository,
                             SongRepository songRepository,
                             ModelMapper mapper) {
        this.artistRepository = artistRepository;
        this.songRepository = songRepository;
        this.mapper = mapper;
        log.info("ArtistServiceImpl инициализирован");
    }

    @Override
    @Transactional
    @CacheEvict(cacheNames = "artists", allEntries = true)
    public void addArtist(AddArtistDto artistDTO) {
        log.debug("Добавление нового артиста: {}", artistDTO.getName());

        String baseSlug = Transliteration.transliterateToLatin(artistDTO.getName())
                .toLowerCase()
                .replaceAll("[^a-z0-9]+", "-")
                .replaceAll("^-|-$", "");

        if (baseSlug.isEmpty()) baseSlug = "artist";

        String uniqueSlug = baseSlug;
        int counter = 1;

        while (artistRepository.existsBySlug(uniqueSlug)) {
            uniqueSlug = baseSlug + "-" + counter;
            counter++;
        }

        Artist artist = mapper.map(artistDTO, Artist.class);
        artist.setSlug(uniqueSlug);

        artistRepository.save(artist);
        log.info("Артист успешно добавлен: {} с псевдонимом: {}", artistDTO.getName(), uniqueSlug);
    }

    @Override
    @Cacheable(value = "artists", key = "'all'")
    public List<ShowArtistInfoDto> allArtists() {
        log.debug("Получение списка всех артистов");
        List<ShowArtistInfoDto> artists = artistRepository.findAll().stream()
                .map(artist -> mapper.map(artist, ShowArtistInfoDto.class))
                .collect(Collectors.toList());
        log.debug("Найдено артистов: {}", artists.size());
        return artists;
    }

    @Override
    public ShowDetailedArtistInfoDto artistInfo(String slug) {
        log.debug("Получение информации об артисте: {}", slug);
        Optional<Artist> artist = artistRepository.findBySlug(slug);

        if (artist.isEmpty()) {
            log.warn("Сотрудник не найден: {}", slug);
            throw new ArtistNotFoundException("Артист '" + slug + "' не найден");
        }

        return mapper.map(artist, ShowDetailedArtistInfoDto.class);
    }

    @Override
    @Transactional
    @CacheEvict(cacheNames = "artists", key="#artistSlug")
    public void deleteArtist(String artistSlug) {
        log.debug("Удаление артиста: {}", artistSlug);

        Optional<Artist> artist = artistRepository.findBySlug(artistSlug);
        if (artist == null) {
            log.warn("Попытка удалить несуществующего артиста: {}", artistSlug);
            throw new ArtistNotFoundException("Артист со slug '" + artistSlug + "' не найден");
        }

        List<Song> songs = artist.get().getSongs();
        if (songs != null && !songs.isEmpty()) {
            for (Song song : songs) {
                song.setArtist(null);
                songRepository.save(song);
            }
        }

        artistRepository.deleteById(artist.get().getId());
        log.info("Артист удален: {}", artistSlug);
    }

    @Override
    public Long getTotalArtistsCount() {
        return artistRepository.getTotalArtistsCount();
    }



}