package com.example.bookstore_interactive.dto.user;

import com.example.bookstore_interactive.dto.song.ShowSongInfoDto;
import com.example.bookstore_interactive.models.entities.Role;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class ShowUserInfoDto {
    private String username;
    private String email;
    private List<ShowSongInfoDto> createdSongs;
    private List<Role> roles;
}
