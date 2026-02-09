package com.example.bookstore_interactive.services.interfaces;

import com.example.bookstore_interactive.dto.artist.ShowArtistInfoDto;
import com.example.bookstore_interactive.dto.user.ShowDetailedUserInfoDto;
import com.example.bookstore_interactive.dto.user.ShowUserInfoDto;
import com.example.bookstore_interactive.models.entities.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface UserService {

    // Основные CRUD операции
    List<ShowUserInfoDto> allUsers();

    ShowDetailedUserInfoDto userInfo(String username);

    Optional<User> findByUsername(String username);

    User getUserById(String id);

    User getUserByUsername(String username);

    User getUserByEmail(String email);

    User updateUser(String userId, User updatedUser);

    void deleteUser(String userId);

    Long getTotalUsersCount();
}