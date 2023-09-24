package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserDto;

import javax.validation.Valid;

@Controller
@RequestMapping(path = "/users")
@RequiredArgsConstructor
@Slf4j
@Validated
public class UserController {

    private final UserClient userClient;

    @PostMapping
    public ResponseEntity<Object> addUser(@Valid @RequestBody UserDto userDto) {
        log.info("Create user {}", userDto);
        return userClient.addUser(userDto);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getUser(@PathVariable long id) {
        log.info("Get user {}", id);
        return userClient.getUser(id);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Object> updateUser(@PathVariable long id, @Valid @RequestBody UserDto userDto) {
        log.info("Update user {}, userId={}", userDto, id);
        return userClient.updateUser(id, userDto);
    }

    @DeleteMapping("/{id}")
    public void removeUser(@PathVariable long id) {
        log.info("Remove user {}", id);
        userClient.removeUser(id);
    }

    @GetMapping
    public ResponseEntity<Object> getUsers() {
        log.info("Get users");
        return userClient.getUsers();
    }

}
