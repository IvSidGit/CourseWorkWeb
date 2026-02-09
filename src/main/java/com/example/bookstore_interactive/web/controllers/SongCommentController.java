package com.example.bookstore_interactive.web.controllers;

import org.springframework.ui.Model;
import com.example.bookstore_interactive.dto.artist.AddArtistDto;
import com.example.bookstore_interactive.dto.comment.AddCommentDto;
import com.example.bookstore_interactive.repositories.SongCommentRepository;
import com.example.bookstore_interactive.services.interfaces.SongCommentService;
import com.example.bookstore_interactive.services.interfaces.UserService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;

@Slf4j
@Controller
@RequestMapping("/song-comments")
public class SongCommentController {
    SongCommentService songCommentService;

    public SongCommentController(SongCommentService songCommentService) {
        this.songCommentService = songCommentService;
    }

    @DeleteMapping("/comment-delete/{commentId}")
    public String deleteSongComment(@PathVariable("commentId") String commentId) {
        log.debug("Удаление комментария через контроллер: {}", commentId);
        String songSlug = songCommentService.getCommentById(commentId).getSong().getSlug();
        songCommentService.deleteComment(commentId);
        log.info("Комментарий удален через контроллер: {}", commentId);

        return "redirect:/songs/song-details/" + songSlug;
    }



    @GetMapping("/add")
    public String addComment(@RequestParam String songSlug, Model model, Principal principal) {
        log.debug("Отображение формы добавления комментария для песни: {}", songSlug);

        AddCommentDto commentDto = new AddCommentDto();
        commentDto.setSongSlug(songSlug);

        if (principal != null) {
            commentDto.setUsername(principal.getName());
        }

        model.addAttribute("commentModel", commentDto);
        return "comment-add";
    }

    @PostMapping("/add")
    public String addComment(@Valid @ModelAttribute("commentModel") AddCommentDto commentModel,
                             BindingResult bindingResult,
                             Model model, // ← добавить Model для возврата на форму
                             RedirectAttributes redirectAttributes) {

        log.debug("Обработка POST запроса на добавление комментария: {}", commentModel);

        if (bindingResult.hasErrors()) {
            log.warn("Ошибки валидации: {}", bindingResult.getAllErrors());
            // Возвращаем на форму с ошибками
            return "comment-add"; // ← не редирект, а возврат шаблона!
        }

        songCommentService.addComment(commentModel);
        redirectAttributes.addFlashAttribute("successMessage",
                "Комментарий успешно добавлен!");

        return "redirect:/songs/song-details/" + commentModel.getSongSlug();
    }
}