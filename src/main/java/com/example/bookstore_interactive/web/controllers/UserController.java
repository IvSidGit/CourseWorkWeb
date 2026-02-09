package com.example.bookstore_interactive.web.controllers;

import com.example.bookstore_interactive.services.interfaces.ArtistService;
import com.example.bookstore_interactive.services.interfaces.SongService;
import com.example.bookstore_interactive.services.interfaces.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Arrays;

@Slf4j
@Controller
@RequestMapping("/users")
public class UserController {
    UserService userService;
    SongService songService;

    public UserController(UserService userService, SongService songService) {
        this.userService = userService;
        this.songService = songService;
    }

    @GetMapping("/all")
    public String showAllUsers(Model model) {
        log.debug("Отображение списка всех пользователей");
        model.addAttribute("allUsers", userService.allUsers());

        return "user-all";
    }

    @GetMapping("/add")
    public String addArtist() {
        log.debug("Отображение формы добавления пользователя");
        return "user-add";
    }

    @GetMapping("/user-details/{username}")
    public String showUserDetails(@PathVariable("username") String username, Model model) {
        log.debug("Отображение деталей пользователя: {}", username);

        model.addAttribute("userDetails", userService.userInfo(username));

        return "user-details";
    }


}
