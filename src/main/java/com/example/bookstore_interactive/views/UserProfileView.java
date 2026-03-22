package com.example.bookstore_interactive.views;

import com.example.bookstore_interactive.dto.song.ShowSongInfoDto;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class UserProfileView {
    private String username;

    private String email;

    private String fullName;

    private int age;

    private List<ShowSongInfoDto> createdSongs;

    public UserProfileView() {
    }

    public UserProfileView(String username, String email, String fullName, int age, List<ShowSongInfoDto> createdSongs) {
        this.username = username;
        this.email = email;
        this.fullName = fullName;
        this.age = age;
        this.createdSongs = createdSongs;
    }

}
