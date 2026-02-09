package com.example.bookstore_interactive.web.controllers;

import com.example.bookstore_interactive.services.interfaces.SongService;
import com.example.bookstore_interactive.services.interfaces.ArtistService;
import com.example.bookstore_interactive.services.interfaces.UserService;
import com.example.bookstore_interactive.services.interfaces.SongCommentService;
import com.example.bookstore_interactive.dto.HomeStatsDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.Principal;

@Slf4j
@Controller
public class HomeController {

    private final SongService songService;
    private final ArtistService artistService;
    private final UserService userService;
    private final SongCommentService commentService;

    public HomeController(SongService songService,
                          ArtistService artistService,
                          UserService userService,
                          SongCommentService commentService) {
        this.songService = songService;
        this.artistService = artistService;
        this.userService = userService;
        this.commentService = commentService;
    }

    @GetMapping("/")
    public String home(Model model, Principal principal) {
        log.debug("Отображение главной страницы");

        // 1. Общая статистика
        HomeStatsDto stats = new HomeStatsDto();
        stats.setSongsCount(songService.getTotalSongsCount());
        stats.setArtistsCount(artistService.getTotalArtistsCount());
        stats.setUsersCount(userService.getTotalUsersCount());
        stats.setTotalViews(songService.getTotalViewsCount());
        stats.setAverageRating(songService.getAverageRating());
        stats.setTotalComments(commentService.getTotalCommentsCount());

        model.addAttribute("stats", stats);

        // 2. Топ контент
        model.addAttribute("topPopularSongs", songService.getTopPopularSongs(5));
        model.addAttribute("recentSongs", songService.getRecentSongs(5));

        // 3. Последние комментарии
        model.addAttribute("recentComments", commentService.getRecentComments(3));


        return "index";
    }
}