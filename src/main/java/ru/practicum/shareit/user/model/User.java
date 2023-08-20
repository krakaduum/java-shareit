package ru.practicum.shareit.user.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@AllArgsConstructor
@Data
@NoArgsConstructor
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    Long id;

    @NotBlank
    @NotNull
    @Column(name = "name", nullable = false, length = 255)
    String name;

    @Email
    @NotBlank
    @NotNull
    @Column(name = "email", nullable = false, length = 255)
    String email;

    public User(String name, String email) {
        this.name = name;
        this.email = email;
    }

}
