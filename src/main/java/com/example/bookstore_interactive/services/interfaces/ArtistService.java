package com.example.bookstore_interactive.services.interfaces;

import com.example.bookstore_interactive.dto.artist.*;

import java.util.List;

public interface ArtistService {
    void addArtist(AddArtistDto artistDTO);

    List<ShowArtistInfoDto> allArtists();

    ShowDetailedArtistInfoDto artistInfo(String slug);

    void deleteArtist(String artistSlug);

    Long getTotalArtistsCount();

}