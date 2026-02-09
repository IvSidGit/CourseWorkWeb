package com.example.bookstore_interactive.dto.artist;

import com.example.bookstore_interactive.dto.song.ShowDetailedSongInfoDto;
import java.util.List;

public class ArtistWithSongsDto extends ArtistDto {
    private String fullDescription;
    private List<ShowDetailedSongInfoDto> popularSongs;
    private List<ShowDetailedSongInfoDto> allSongs;
    private List<String> genres; // Уникальные жанры песен артиста
    private List<String> allChords; // Все аккорды, используемые артистом

    // Getters and Setters
    public String getFullDescription() {
        return fullDescription;
    }

    public void setFullDescription(String fullDescription) {
        this.fullDescription = fullDescription;
    }

    public List<ShowDetailedSongInfoDto> getPopularSongs() {
        return popularSongs;
    }

    public void setPopularSongs(List<ShowDetailedSongInfoDto> popularSongs) {
        this.popularSongs = popularSongs;
    }

    public List<ShowDetailedSongInfoDto> getAllSongs() {
        return allSongs;
    }

    public void setAllSongs(List<ShowDetailedSongInfoDto> allSongs) {
        this.allSongs = allSongs;
    }

    public List<String> getGenres() {
        return genres;
    }

    public void setGenres(List<String> genres) {
        this.genres = genres;
    }

    public List<String> getAllChords() {
        return allChords;
    }

    public void setAllChords(List<String> allChords) {
        this.allChords = allChords;
    }
}