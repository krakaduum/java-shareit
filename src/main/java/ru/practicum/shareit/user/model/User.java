package ru.practicum.shareit.user.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@AllArgsConstructor
@Data
public class User {

    long id;

    String name;

    @Email
    @NotNull
    @NotBlank
    String email;

    public User(String name, String email) {
        this.name = name;
        this.email = email;
    }

}
