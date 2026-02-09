package com.example.bookstore_interactive.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class HomeStatsDto {
    private Long songsCount;          // Общее количество песен
    private Long artistsCount;        // Общее количество артистов
    private Long usersCount;          // Общее количество пользователей
    private Long totalViews;          // Общее количество просмотров
    private Double averageRating;     // Средний рейтинг всех песен
    private Long totalComments;       // Общее количество комментариев

    // Можно добавить другие статистические данные:
    // private Long songsAddedThisWeek; // Песен добавлено за неделю
    // private Long activeUsersToday;   // Активных пользователей сегодня
    // private Map<String, Long> songsByGenre; // Распределение по жанрам
}