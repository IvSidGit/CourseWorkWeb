package com.example.bookstore_interactive.web.controllers;

import com.example.bookstore_interactive.dto.artist.AddArtistDto;
import com.example.bookstore_interactive.services.interfaces.ArtistService;
import com.example.bookstore_interactive.services.interfaces.SongService;
import jakarta.validation.Valid;
import org.springframework.ui.Model;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Slf4j
@Controller
@RequestMapping("/artists")
public class ArtistController {
    ArtistService artistService;
    SongService songService;

    public ArtistController(ArtistService artistService, SongService songService) {
        this.artistService = artistService;
        this.songService = songService;
        log.info("ArtistController инициализирован");
    }

    @GetMapping("/all")
    public String showAllArtists(Model model) {
        log.debug("Отображение списка всех артистов");
        model.addAttribute("allArtists", artistService.allArtists());

        return "artist-all";
    }

    @GetMapping("/add")
    public String addArtist() {
        log.debug("Отображение формы добавления артиста");
        return "artist-add";
    }

    @ModelAttribute("artistModel")
    public AddArtistDto initArtist() {
        return new AddArtistDto();
    }

    @PostMapping("/add")
    public String addCompany(@Valid AddArtistDto artistModel,
                             BindingResult bindingResult,
                             RedirectAttributes redirectAttributes) {
        log.debug("Обработка POST запроса на добавление артиста");

        if (bindingResult.hasErrors()) {
            log.warn("Ошибки валидации при добавлении артиста: {}", bindingResult.getAllErrors());
            redirectAttributes.addFlashAttribute("artistModel", artistModel);
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.artistModel",
                    bindingResult);
            return "redirect:/artists/add";
        }

        artistService.addArtist(artistModel);
        redirectAttributes.addFlashAttribute("successMessage",
                "Артист '" + artistModel.getName() + "' успешно добавлен!");

        return "redirect:/artists/all";
    }

    @GetMapping("/artist-details/{artist-slug}")
    public String showEmployeeDetails(@PathVariable("artist-slug") String artistSlug, Model model) {
        log.debug("Отображение деталей артиста: {}", artistSlug);
        model.addAttribute("artistDetails", artistService.artistInfo(artistSlug));

        return "artist-details";
    }

    @DeleteMapping("/artist-delete/{artistSlug}")
    public String fireEmployee(@PathVariable("artistSlug") String artistSlug) {
        log.debug("Удаление артиста через контроллер: {}", artistSlug);
        artistService.deleteArtist(artistSlug);
        log.info("Артист удален через контроллер: {}", artistSlug);

        return "redirect:/artists/all";
    }

}
