package com.example.bookstore_interactive.models.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@Entity
@Table(name = "users")
public class User extends BaseEntity implements Serializable {

    @Column(nullable = false, unique = true, length = 50)
    private String username;

    @Column(nullable = false)
    private String password;

    @Email
    @Column(unique = true, nullable = false, length = 100)
    private String email;

    @Column(name = "full_name", nullable = false, length = 100)
    private String fullName;

    @Column(nullable = false)
    @Min(value = 0, message = "Возраст не может быть меньше 0!")
    @Max(value = 90, message = "Возраст не может быть больше 90!")
    private int age;

    // Комментарии пользователя
    @OneToMany(mappedBy = "user")
    private List<SongComment> comments = new ArrayList<>();

    // Рейтинги пользователя
    @OneToMany(mappedBy = "user")
    private List<SongRating> ratings = new ArrayList<>();

    // Созданные песни (если нужно)
    @OneToMany(mappedBy = "createdBy")
    private List<Song> createdSongs = new ArrayList<>();

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "users_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private List<Role> roles = new ArrayList<>();

    public User() {
    }

    public User(String username, String password, String email, String fullName, int age) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.fullName = fullName;
        this.age = age;
    }
}
