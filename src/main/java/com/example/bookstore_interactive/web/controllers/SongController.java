package com.example.bookstore_interactive.web.controllers;

import com.example.bookstore_interactive.dto.rating.AddRatingDto;
import com.example.bookstore_interactive.dto.rating.RatingDto;
import com.example.bookstore_interactive.dto.song.AddSongDto;
import com.example.bookstore_interactive.dto.song.ShowDetailedSongInfoDto;
import com.example.bookstore_interactive.models.entities.Song;
import com.example.bookstore_interactive.models.entities.User;
import com.example.bookstore_interactive.repositories.SongRepository;
import com.example.bookstore_interactive.services.interfaces.ArtistService;
import com.example.bookstore_interactive.services.interfaces.SongRatingService;
import com.example.bookstore_interactive.services.interfaces.SongService;
import com.example.bookstore_interactive.services.interfaces.UserService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Slf4j
@Controller
@RequestMapping("/songs")
public class SongController {
    UserService userService;
    ArtistService artistService;
    SongService songService;
    SongRatingService songRatingService;
    SongRepository songRepository;

    public SongController(UserService userService, ArtistService artistService,
                          SongService songService, SongRatingService songRatingService,
                          SongRepository songRepository) {
        this.userService = userService;
        this.artistService = artistService;
        this.songService = songService;
        this.songRatingService = songRatingService;
        this.songRepository = songRepository;
    }

    @GetMapping("/all")
    public String showAllSongs(Model model) {
        log.debug("Отображение списка всех песен");
        model.addAttribute("allSongs", songService.allSongs());

        return "song-all";
    }

    @GetMapping("/add")
    public String addSong(Model model) {
        log.debug("Отображение формы добавления песни");
        model.addAttribute("availableArtists", artistService.allArtists());
        model.addAttribute("availableUsers", userService.allUsers());

        return "song-add";
    }

    @ModelAttribute("songModel")
    public AddSongDto initSong() {
        return new AddSongDto();
    }

    @PostMapping("/add")
    public String addSong(@Valid AddSongDto songModel,
                          BindingResult bindingResult,
                          RedirectAttributes redirectAttributes,
                          Authentication authentication) {

        log.debug("Обработка добавления песни: {}", songModel.getTitle());

        if (bindingResult.hasErrors()) {
            log.warn("Ошибки валидации при добавлении песни: {}", bindingResult.getAllErrors());
            redirectAttributes.addFlashAttribute("songModel", songModel);
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.songModel",
                    bindingResult);
            return "redirect:/songs/add";
        }

        String username = authentication.getName();

        User currentUser = userService.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Пользователь не найден"));

        songModel.setUserId(currentUser.getId());

        songService.addSong(songModel);
        log.info("Песня успешно добавлена через контроллер");

        return "redirect:/songs/all";
    }


    @GetMapping("/song-details/{song-slug}")
    public String showSongDetails(@PathVariable("song-slug") String songSlug,
                                  Model model,
                                  Authentication authentication) {
        log.debug("Отображение деталей песни: {}", songSlug);


        ShowDetailedSongInfoDto songDetails = songService.songInfo(songSlug);
        model.addAttribute("songDetails", songDetails);

        if (authentication != null && authentication.isAuthenticated()) {
            String username = authentication.getName();
            User user = userService.findByUsername(username).orElse(null);

            if (user != null) {
                boolean hasUserRated = songRatingService.hasUserRatedSong(songDetails.getId(), user.getId());
                model.addAttribute("hasUserRated", hasUserRated);

                if (hasUserRated) {
                    RatingDto userRatingDto = songRatingService.getUserRatingForSong(songDetails.getId(), user.getId());
                    model.addAttribute("userRating", userRatingDto.getRating());
                }
            }
        }

        return "song-details";
    }

    @PostMapping("/rate-song")
    public String rateSong(@RequestParam String songId,
                           @RequestParam Integer rating,
                           Authentication authentication,
                           RedirectAttributes redirectAttributes) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return "redirect:/login";
        }

        String username = authentication.getName();
        User user = userService.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Пользователь не найден"));

        AddRatingDto ratingDto = new AddRatingDto();
        ratingDto.setSongId(songId);
        ratingDto.setRating(rating);

        songRatingService.addOrUpdateRating(ratingDto, user.getId());

        Song song = songRepository.findById(songId)
                .orElseThrow(() -> new RuntimeException("Песня не найдена"));

        songRepository.saveAndFlush(song);

        return "redirect:/songs/song-details/" + song.getSlug();
    }

    @DeleteMapping("/delete-rating")
    public String deleteRating(@RequestParam String songId,
                               Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return "redirect:/login";
        }

        String username = authentication.getName();
        User user = userService.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Пользователь не найден"));

        songRatingService.deleteRating(songId, user.getId());

        Song song = songRepository.findById(songId)
                .orElseThrow(() -> new RuntimeException("Песня не найдена"));

        return "redirect:/songs/song-details/" + song.getSlug();
    }

    @GetMapping("/random")
    public String showRandomSong() {
        String songSlug = songService.getRandomSlug();
        log.debug("Отображение случайной песни: {}", songSlug);

        return "redirect:/songs/song-details/" + songSlug;
    }

    @DeleteMapping("/song-delete/{songId}")
    public String fireEmployee(@PathVariable("songId") String songId) {
        log.debug("Удаление песни через контроллер: {}", songId);
        songService.deleteSong(songId);
        log.info("Песня удалена через контроллер: {}", songId);

        return "redirect:/songs/all";
    }

    @GetMapping("/search")
    public String searchSongs(@RequestParam(required = false) String query, Model model) {
        if (query != null && !query.isEmpty()) {
            model.addAttribute("searchResults",
                    songRepository.findByTitleContainingIgnoreCase(query));
            model.addAttribute("searchQuery", query);
        }
        return "songs-search-results";
    }
}