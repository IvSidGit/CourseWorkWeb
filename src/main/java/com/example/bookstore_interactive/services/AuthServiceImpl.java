package com.example.bookstore_interactive.services;

import com.example.bookstore_interactive.dto.song.ShowSongInfoDto;
import com.example.bookstore_interactive.dto.user.UserRegistrationDto;
import com.example.bookstore_interactive.models.entities.User;
import com.example.bookstore_interactive.models.enums.UserRoles;
import com.example.bookstore_interactive.models.exceptions.UserNotFoundException;
import com.example.bookstore_interactive.repositories.UserRepository;
import com.example.bookstore_interactive.repositories.UserRoleRepository;
import com.example.bookstore_interactive.services.interfaces.AuthService;
import com.example.bookstore_interactive.views.UserProfileView;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final UserRoleRepository userRoleRepository;
    private final PasswordEncoder passwordEncoder;
    private final ModelMapper mapper;

    public AuthServiceImpl(UserRepository userRepository, UserRoleRepository userRoleRepository, PasswordEncoder passwordEncoder, ModelMapper mapper) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.userRoleRepository = userRoleRepository;
        this.mapper = mapper;
    }

    @Override
    @Transactional
    public void register(UserRegistrationDto registrationDTO) {
        if (!registrationDTO.getPassword().equals(registrationDTO.getConfirmPassword())) {
            throw new RuntimeException("passwords.match");
        }

        if (userRepository.findByEmail(registrationDTO.getEmail()).isPresent()) {
            throw new RuntimeException("email.used");
        }

        var userRole = userRoleRepository.findRoleByName(UserRoles.USER).orElseThrow();

        User user = new User(
                registrationDTO.getUsername(),
                passwordEncoder.encode(registrationDTO.getPassword()),
                registrationDTO.getEmail(),
                registrationDTO.getFullname(),
                registrationDTO.getAge()
        );

        user.setRoles(List.of(userRole));

        userRepository.save(user);
    }

    @Override
    public User getUser(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(username + " was not found!"));
    }

    // В сервисе
    @Override
    @Transactional(readOnly = true)
    public UserProfileView getUserProfile(String userId) {
        User user = userRepository.findWithAddedSongs(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        return new UserProfileView(
                user.getUsername(),
                user.getEmail(),
                user.getFullName(),
                user.getAge(),
                user.getCreatedSongs().stream()
                        .map(song -> mapper.map(song, ShowSongInfoDto.class))
                        .toList()
        );
    }
}
