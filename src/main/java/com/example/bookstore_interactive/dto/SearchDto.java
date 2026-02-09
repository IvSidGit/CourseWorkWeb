package com.example.bookstore_interactive.dto;

import java.util.List;

public class SearchDto {
    private String query;
    private String searchType; // "all", "songs", "artists", "chords"
    private String genre;
    private String artist;
    private List<String> chords;
    private Integer minRating;
    private String sortBy; // "relevance", "rating", "views", "newest"
    private Boolean onlyWithChords;

    // Getters and Setters
    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public String getSearchType() {
        return searchType;
    }

    public void setSearchType(String searchType) {
        this.searchType = searchType;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public List<String> getChords() {
        return chords;
    }

    public void setChords(List<String> chords) {
        this.chords = chords;
    }

    public Integer getMinRating() {
        return minRating;
    }

    public void setMinRating(Integer minRating) {
        this.minRating = minRating;
    }

    public String getSortBy() {
        return sortBy;
    }

    public void setSortBy(String sortBy) {
        this.sortBy = sortBy;
    }

    public Boolean getOnlyWithChords() {
        return onlyWithChords;
    }

    public void setOnlyWithChords(Boolean onlyWithChords) {
        this.onlyWithChords = onlyWithChords;
    }
}