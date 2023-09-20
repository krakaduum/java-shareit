package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.shareit.user.dto.UserDto;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.assertEquals;

@RequiredArgsConstructor(onConstructor_ = @Autowired)
@SpringBootTest
public class UserServiceIntegrationTest {

    private final UserService userService;

    @Test
    public void addUserTest() {
        // Arrange
        var userDto = makeUserDto(1L,
                "Foo Bar",
                "foo.bar@mail.com");

        // Act
        var addedUserDto = userService.addUser(userDto);

        // Assert
        assertThat(addedUserDto.getId(), notNullValue());
        assertEquals(userDto.getName(), addedUserDto.getName());
        assertEquals(userDto.getEmail(), addedUserDto.getEmail());
    }

    private UserDto makeUserDto(long id, String name, String email) {
        return new UserDto(id, name, email);
    }

}
