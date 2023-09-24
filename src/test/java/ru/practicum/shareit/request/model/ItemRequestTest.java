package ru.practicum.shareit.request.model;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class ItemRequestTest {

    @Test
    public void constructor_withValidData_returnsItemRequest() {
        // Arrange
        long id = 1L;
        String description = "Description";
        var requester = new User(1L, "Requester Name", "requester.name@mail.com");
        LocalDateTime created = LocalDateTime.now();

        // Act
        var itemRequest = new ItemRequest(id, description, requester, created, null);

        // Assert
        assertEquals(id, itemRequest.getId());
        assertEquals(description, itemRequest.getDescription());
        assertEquals(requester.getId(), itemRequest.getRequester().getId());
        assertEquals(created, itemRequest.getCreated());
    }

}
