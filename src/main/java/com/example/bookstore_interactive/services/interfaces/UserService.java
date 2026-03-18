package com.example.bookstore_interactive.services.interfaces;

import com.example.bookstore_interactive.dto.user.ShowDetailedUserInfoDto;
import com.example.bookstore_interactive.dto.user.ShowUserInfoDto;
import com.example.bookstore_interactive.models.entities.User;

import java.util.List;
import java.util.Optional;

public interface UserService {

    // Основные CRUD операции
    List<ShowUserInfoDto> allUsers();

    ShowDetailedUserInfoDto userInfo(String username);

    Optional<User> findByUsername(String username);

    Long getTotalUsersCount();
}