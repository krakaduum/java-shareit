package ru.practicum.shareit.user.model;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class UserTest {

    @Test
    public void constructor_withValidData_returnsUser() {
        // Arrange
        long id = 1L;
        String name = "User Name";
        String email = "user.name@mail.com";

        // Act
        var user = new User(id, name, email);

        // Assert
        assertEquals(id, user.getId());
        assertEquals(name, user.getName());
        assertEquals(email, user.getEmail());
    }

}
