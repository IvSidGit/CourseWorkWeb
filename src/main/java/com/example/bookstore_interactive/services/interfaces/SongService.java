package com.example.bookstore_interactive.services.interfaces;

import com.example.bookstore_interactive.dto.artist.AddArtistDto;
import com.example.bookstore_interactive.dto.artist.ShowArtistInfoDto;
import com.example.bookstore_interactive.dto.artist.ShowDetailedArtistInfoDto;
import com.example.bookstore_interactive.dto.song.AddSongDto;
import com.example.bookstore_interactive.dto.song.ShowDetailedSongInfoDto;
import com.example.bookstore_interactive.dto.song.ShowSongInfoDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface SongService {


    void addSong(AddSongDto songDto);

    List<ShowSongInfoDto> allSongs();

    ShowDetailedSongInfoDto songInfo(String songSlug);
    void incrementViews(String songId);

    // Добавление новой песни (с проверкой/созданием исполнителя)
    ShowDetailedSongInfoDto addSong(AddSongDto songDto, String userId);

    // Получение песни по slug (для URL)
    ShowSongInfoDto getSongBySlug(String slug);

    // Получение песни по ID
    ShowDetailedSongInfoDto getSongById(String id);

    // Поиск песен по различным параметрам
    Page<ShowDetailedSongInfoDto> searchSongsByTitle(String title, Pageable pageable);
    Page<ShowDetailedSongInfoDto> searchSongsByArtist(String artistName, Pageable pageable);
    Page<ShowDetailedSongInfoDto> searchSongsByGenre(String genre, Pageable pageable);
    Page<ShowDetailedSongInfoDto> searchSongsByChord(String chord, Pageable pageable);
    Page<ShowDetailedSongInfoDto> searchSongs(String searchTerm, Pageable pageable);

    // Получение песен пользователя
    Page<ShowDetailedSongInfoDto> getSongsByUser(String userId, Pageable pageable);

    // Получение топ-песен
    Page<ShowDetailedSongInfoDto> getTopRatedSongs(Pageable pageable);
    Page<ShowDetailedSongInfoDto> getMostViewedSongs(Pageable pageable);
    Page<ShowDetailedSongInfoDto> getNewestSongs(Pageable pageable);

    // Обновление информации о песне
    ShowDetailedSongInfoDto updateSong(String songId, AddSongDto songDto, String userId);

    // Удаление песни
    void deleteSong(String songId);


    String getRandomSlug();


    // Изменение статуса песни (активна/неактивна)
    void changeSongStatus(String songId, String status, String userId);

    // Проверка существования песни по slug
    boolean songExistsBySlug(String slug);

    // Получение песен по аккордам (для подбора)
    Page<ShowDetailedSongInfoDto> getSongsByChords(List<String> chords, Pageable pageable);

    // Статистика
    Long getTotalSongsCount();
    Long getTotalViewsCount();
    Double getAverageRating();

    // Топ контент
    List<ShowDetailedSongInfoDto> getTopPopularSongs(int limit);
    List<ShowDetailedSongInfoDto> getRecentSongs(int limit);


}