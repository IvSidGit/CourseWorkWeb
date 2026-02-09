package com.example.bookstore_interactive.web.controllers;

import ch.qos.logback.core.model.Model;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/song-ratings")
public class SongRatingController {
//    ArtistService artistService = new ArtistService();
//    ArtistService artistService = new ArtistService();

    @GetMapping("/")
    public String showAllEmployees(Model model) {
//        log.debug("Отображение списка всех сотрудников");
//        model.addAttribute("allEmployees", artistService.getAllArtists());
        return "employee-all";
    }

}