
package com.example.bookstore_interactive.dto.user;

import com.example.bookstore_interactive.dto.song.ShowSongInfoDto;
import com.example.bookstore_interactive.models.entities.Role;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
public class ShowDetailedUserInfoDto {
    private String username;
    private String email;
    private String fullName;
    private Integer age;
    private List<ShowSongInfoDto> createdSongs;
    private List<Role> roles;

    public ShowDetailedUserInfoDto(String username, String email, String fullName, Integer age, List<ShowSongInfoDto> createdSongs, List<Role> roles) {
        this.username = username;
        this.email = email;
        this.fullName = fullName;
        this.age = age;
        this.createdSongs = createdSongs;
        this.roles = roles;
    }

    public ShowDetailedUserInfoDto() {
    }
}
