package ru.practicum.shareit.user.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.List;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;

@SpringBootTest
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    private UserService userService;

    @BeforeEach
    public void setUp() {
        initMocks(this);
        userService = new UserServiceImpl(userRepository);
    }

    @Test
    public void addUserTest() {
        // Arrange
        var userDto = makeUserDto(1L,
                "Foo Bar",
                "foo.bar@mail.com");
        var user = makeUser(1L,
                "Foo Bar",
                "foo.bar@mail.com");

        when(userRepository.save(any()))
                .thenReturn(user);

        // Act
        var addedUserDto = userService.addUser(userDto);

        // Assert
        assertThat(addedUserDto.getId(), notNullValue());
        assertEquals(userDto.getName(), addedUserDto.getName());
        assertEquals(userDto.getEmail(), addedUserDto.getEmail());
    }

    @Test
    public void getUserTest() {
        // Arrange
        var user = makeUser(1L,
                "Foo Bar",
                "foo.bar@mail.com");

        when(userRepository.findById(anyLong()))
                .thenReturn(Optional.of(user));

        // Act
        var userDto = userService.getUser(1);

        // Assert
        assertThat(userDto.getId(), notNullValue());
        assertEquals(userDto.getName(), userDto.getName());
        assertEquals(userDto.getEmail(), userDto.getEmail());
    }

    @Test
    public void updateUserTest() {
        // Arrange
        var userDto = makeUserDto(1L,
                "Foo Bar",
                "foo.bar@mail.com");
        var user = makeUser(1L,
                "Foo Bar",
                "foo.bar@mail.com");

        when(userRepository.findById(anyLong()))
                .thenReturn(Optional.of(user));
        when(userRepository.save(any()))
                .thenReturn(user);

        // Act
        var updatedUserDto = userService.updateUser(1, userDto);

        // Assert
        assertThat(updatedUserDto.getId(), notNullValue());
        assertEquals(userDto.getName(), updatedUserDto.getName());
        assertEquals(userDto.getEmail(), updatedUserDto.getEmail());
    }

    @Test
    public void removeUserTest() {
        // Arrange
        when(userRepository.existsById(anyLong()))
                .thenReturn(true);

        // Act
        userService.removeUser(1);

        // Assert
        verify(userRepository, times(1))
                .existsById(anyLong());
        verify(userRepository, times(1))
                .deleteById(anyLong());
    }

    @Test
    public void getUsersTest() {
        // Arrange
        var user = makeUser(1L,
                "Foo Bar",
                "foo.bar@mail.com");

        when(userRepository.findAll())
                .thenReturn(List.of(user));

        // Act
        var users = userService.getUsers();
        var firstUser = users.stream().findFirst();

        // Assert
        assertEquals(users.size(), 1);
        assertThat(firstUser.get().getId(), notNullValue());
        assertEquals(firstUser.get().getName(), user.getName());
        assertEquals(firstUser.get().getEmail(), user.getEmail());
    }

    private User makeUser(long id, String name, String email) {
        return new User(id, name, email);
    }

    private UserDto makeUserDto(long id, String name, String email) {
        return new UserDto(id, name, email);
    }

}
