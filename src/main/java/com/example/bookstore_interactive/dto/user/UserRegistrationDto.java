package com.example.bookstore_interactive.dto.user;

import com.example.bookstore_interactive.utils.validation.*;
import jakarta.validation.constraints.*;
import lombok.Setter;

@Setter
public class UserRegistrationDto {

    @UniqueUsername
    private String username;

    private String fullname;

    @UniqueEmail
    private String email;

    private int age;

    private String password;

    private String confirmPassword;


    public UserRegistrationDto() {
    }

    @NotEmpty(message = "Имя пользователя не должно быть пустым!")
    @Size(min = 5, max = 20, message = "Имя пользователя должно быть от 5 до 20 символов!")
    public String getUsername() {
        return username;
    }

    @NotEmpty(message = "Полное имя не должно быть пустым!")
    @Size(min = 5, max = 20, message = "Полное имя должно быть от 5 до 20 символов!")
    public String getFullname() {
        return fullname;
    }

    @NotEmpty(message = "Email не должен быть пустым!")
    @Email(message = "Введите корректный email!")
    public String getEmail() {
        return email;
    }

    @Min(value = 0, message = "Возраст не может быть меньше 0!")
    @Max(value = 90, message = "Возраст не может быть больше 90!")
    public int getAge() {
        return age;
    }

    @NotEmpty(message = "Пароль не должен быть пустым!")
    @Size(min = 5, max = 20, message = "Пароль должен быть от 5 до 20 символов!")
    public String getPassword() {
        return password;
    }

    @NotEmpty(message = "Подтверждение пароля не должно быть пустым!")
    @Size(min = 5, max = 20, message = "Подтверждение пароля должно быть от 5 до 20 символов!")
    public String getConfirmPassword() {
        return confirmPassword;
    }

    @Override
    public String toString() {
        return "UserRegistrationDTO{" +
                "username='" + username + '\'' +
                ", fullName='" + fullname + '\'' +
                ", email='" + email + '\'' +
                ", age=" + age +
                ", password='" + password + '\'' +
                ", confirmPassword='" + confirmPassword + '\'' +
                '}';
    }
}
