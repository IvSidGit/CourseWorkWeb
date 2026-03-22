package com.example.bookstore_interactive.services.interfaces;

import com.example.bookstore_interactive.dto.song.AddSongDto;
import com.example.bookstore_interactive.dto.song.ShowDetailedSongInfoDto;
import com.example.bookstore_interactive.dto.song.ShowSongInfoDto;
import java.util.List;

public interface SongService {


    void addSong(AddSongDto songDto);

    List<ShowSongInfoDto> allSongs();

    ShowDetailedSongInfoDto songInfo(String songSlug);

    void deleteSong(String songId);

    String getRandomSlug();

    Long getTotalSongsCount();
    Long getTotalViewsCount();
    Double getAverageRating();

    List<ShowDetailedSongInfoDto> getTopPopularSongs(int limit);
    List<ShowDetailedSongInfoDto> getRecentSongs(int limit);


}