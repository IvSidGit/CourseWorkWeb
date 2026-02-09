package com.example.bookstore_interactive.services.interfaces;

import com.example.bookstore_interactive.dto.user.UserRegistrationDto;
import com.example.bookstore_interactive.models.entities.User;
import com.example.bookstore_interactive.views.UserProfileView;

public interface AuthService {
    void register(UserRegistrationDto registrationDTO);

    User getUser(String username);

    UserProfileView getUserProfile(String username);
}