package ru.practicum.shareit.user.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import javax.validation.ValidationException;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.openMocks;

@SpringBootTest
public class UserServiceTest {

    private final User user = new User(1L,
            "User Name",
            "user.name@mail.com");

    @Mock
    private UserRepository userRepository;

    private UserService userService;

    @BeforeEach
    public void setUp() {
        openMocks(this);
        userService = new UserServiceImpl(userRepository);
    }

    @Test
    public void addUser_withValidUserDto_returnsUserDto() throws ValidationException {
        // Arrange
        var userDto = new UserDto(1L,
                "User Name",
                "user.name@mail.com");

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
    public void addUser_withEmptyName_throwsValidationException() throws ValidationException {
        // Arrange
        var userDto = new UserDto(1L,
                "",
                "user.name@mail.com");

        // Act & Assert
        assertThrows(ValidationException.class, () -> userService.addUser(userDto));
    }

    @Test
    public void addUser_withEmptyEmail_throwsValidationException() throws ValidationException {
        // Arrange
        var userDto = new UserDto(1L,
                "User Name",
                "");

        // Act & Assert
        assertThrows(ValidationException.class, () -> userService.addUser(userDto));
    }

    @Test
    public void getUser_withValidUserId_returnsUserDto() throws NoSuchElementException {
        // Arrange
        when(userRepository.findById(anyLong()))
                .thenReturn(Optional.of(user));

        // Act
        var foundUserDto = userService.getUser(1L);

        // Assert
        assertThat(user.getId(), notNullValue());
        assertEquals(user.getName(), foundUserDto.getName());
        assertEquals(user.getEmail(), foundUserDto.getEmail());
    }

    @Test
    public void getUser_withInvalidUserId_throwsNoSuchElementException() throws NoSuchElementException {
        // Act & Assert
        assertThrows(NoSuchElementException.class, () -> userService.getUser(1L));
    }

    @Test
    public void updateUser_withValidUserDtoAndValidUserId_returnsUserDto()
            throws NoSuchElementException, ValidationException {
        // Arrange
        var userDto = new UserDto(1L,
                "User Name",
                "user.name@mail.com");

        when(userRepository.findById(anyLong()))
                .thenReturn(Optional.of(user));
        when(userRepository.save(any()))
                .thenReturn(user);

        // Act
        var updatedUserDto = userService.updateUser(1L, userDto);

        // Assert
        assertThat(updatedUserDto.getId(), notNullValue());
        assertEquals(userDto.getName(), updatedUserDto.getName());
        assertEquals(userDto.getEmail(), updatedUserDto.getEmail());
    }

    @Test
    public void updateUser_withInvalidUserId_throwsNoSuchElementException() throws NoSuchElementException {
        // Arrange
        var userDto = new UserDto(1L,
                "User Name",
                "user.name@mail.com");

        // Act & Assert
        assertThrows(NoSuchElementException.class, () -> userService.updateUser(1L, userDto));
    }

    @Test
    public void removeUser_withValidUserId_callsUserRepositoryTwice() throws NoSuchElementException {
        // Arrange
        when(userRepository.existsById(anyLong()))
                .thenReturn(true);

        // Act
        userService.removeUser(1L);

        // Assert
        verify(userRepository, times(1))
                .existsById(anyLong());
        verify(userRepository, times(1))
                .deleteById(anyLong());
    }

    @Test
    public void removeUser_withInvalidUserId_throwsNoSuchElementException() throws NoSuchElementException {
        // Act & Assert
        assertThrows(NoSuchElementException.class, () -> userService.removeUser(1L));
    }

    @Test
    public void getUsers_returnsUsersCollection() {
        // Arrange
        when(userRepository.findAll())
                .thenReturn(List.of(user));

        // Act
        var users = userService.getUsers();
        var firstUser = users.stream().findFirst();

        // Assert
        assertEquals(users.size(), 1);
        assertTrue(firstUser.isPresent());
        assertThat(firstUser.get().getId(), notNullValue());
        assertEquals(user.getName(), firstUser.get().getName());
        assertEquals(user.getEmail(), firstUser.get().getEmail());
    }

}
