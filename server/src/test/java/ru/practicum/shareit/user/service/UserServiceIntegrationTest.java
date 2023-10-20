package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.jdbc.Sql;
import ru.practicum.shareit.user.dto.UserDto;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@RequiredArgsConstructor(onConstructor_ = @Autowired)
@AutoConfigureTestDatabase
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@Sql(scripts = "classpath:schema.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@SpringBootTest
public class UserServiceIntegrationTest {

    private final UserService userService;

    @Test
    public void addUser_withValidUserDto_returnsUserDto() {
        // Arrange
        var userDto = new UserDto(1L,
                "User Name",
                "user.name@mail.com");

        // Act
        var addedUserDto = userService.addUser(userDto);

        // Assert
        assertThat(addedUserDto.getId(), notNullValue());
        assertEquals(userDto.getName(), addedUserDto.getName());
        assertEquals(userDto.getEmail(), addedUserDto.getEmail());
    }

    @Test
    public void addUser_withDuplicateEmail_throwsDataIntegrityViolationException()
            throws DataIntegrityViolationException {
        // Arrange
        var userDto1 = new UserDto(1L,
                "User1 Name",
                "user1.name@mail.com");
        var userDto2 = new UserDto(2L,
                "User2 Name",
                "user1.name@mail.com");

        userService.addUser(userDto1);

        // Act & Assert
        assertThrows(DataIntegrityViolationException.class, () -> userService.addUser(userDto2));
    }

    @Test
    public void updateUser_withDuplicateEmail_throwsDataIntegrityViolationException()
            throws DataIntegrityViolationException {
        // Arrange
        var userDto1 = new UserDto(1L,
                "User1 Name",
                "user1.name@mail.com");
        var userDto2 = new UserDto(2L,
                "User2 Name",
                "user2.name@mail.com");

        userService.addUser(userDto1);
        userService.addUser(userDto2);

        var userDto3 = new UserDto(2L,
                "User2 Name",
                "user1.name@mail.com");

        // Act & Assert
        assertThrows(DataIntegrityViolationException.class, () -> userService.updateUser(2L, userDto3));
    }

}
