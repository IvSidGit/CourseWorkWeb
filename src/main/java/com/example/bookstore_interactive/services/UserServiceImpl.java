package com.example.bookstore_interactive.services;

import com.example.bookstore_interactive.dto.song.ShowSongInfoDto;
import com.example.bookstore_interactive.dto.user.ShowDetailedUserInfoDto;
import com.example.bookstore_interactive.dto.user.ShowUserInfoDto;
import com.example.bookstore_interactive.models.entities.User;
import com.example.bookstore_interactive.models.exceptions.UserNotFoundException;
import com.example.bookstore_interactive.repositories.*;
import com.example.bookstore_interactive.services.interfaces.UserService;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final SongRepository songRepository;
    private final ModelMapper mapper;

    public UserServiceImpl(UserRepository userRepository,
                             SongRepository songRepository,
                             ModelMapper mapper) {
        this.userRepository = userRepository;
        this.songRepository = songRepository;
        this.mapper = mapper;
        log.info("UserServiceImpl инициализирован");
    }


    @Override
    @Cacheable(value = "users", key = "'all'")
    public List<ShowUserInfoDto> allUsers() {
        log.debug("Получение списка всех пользователей");
        List<ShowUserInfoDto> users = userRepository.findAll().stream()
                .map(user -> mapper.map(user, ShowUserInfoDto.class))
                .collect(Collectors.toList());
        log.debug("Найдено пользователей: {}", users.size());
        return users;
    }

    @Override
    @Transactional(readOnly = true)
    public ShowDetailedUserInfoDto userInfo(String username) {

        User search = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        User user = userRepository.findWithAddedSongs(search.getId())
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        return new ShowDetailedUserInfoDto(
                user.getUsername(),
                user.getEmail(),
                user.getFullName(),
                user.getAge(),
                user.getCreatedSongs().stream()
                        .map(song -> mapper.map(song, ShowSongInfoDto.class))
                        .toList(),
                user.getRoles()
        );
    }

    @Override
    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public Long getTotalUsersCount() {
        return userRepository.getTotalUsersCount();
    }



    @Override
    public User getUserById(String id) {
        return null;
    }

    @Override
    public User getUserByUsername(String username) {
        return null;
    }

    @Override
    public User getUserByEmail(String email) {
        return null;
    }

    @Override
    public User updateUser(String userId, User updatedUser) {
        return null;
    }

    @Override
    public void deleteUser(String userId) {

    }

}