package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.jdbc.Sql;
import ru.practicum.shareit.user.dto.UserDto;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.assertEquals;

@RequiredArgsConstructor(onConstructor_ = @Autowired)
@AutoConfigureTestDatabase
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@Sql(scripts = "classpath:schema.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@SpringBootTest
public class UserServiceIntegrationTest {

    private final UserService userService;

    @Test
    public void addUserTest() {
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

}
