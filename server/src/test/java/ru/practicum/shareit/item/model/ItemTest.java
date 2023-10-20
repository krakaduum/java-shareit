package ru.practicum.shareit.item.model;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.shareit.user.model.User;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class ItemTest {

    @Test
    public void constructor_withValidData_returnsItem() {
        // Arrange
        long id = 1L;
        String name = "Item Name";
        String description = "Item Description";
        Boolean available = true;
        var owner = new User(1L, "Owner Name", "owner.name@mail.com");

        // Act
        var item = new Item(1L, name, description, available, owner, null);

        // Assert
        assertEquals(id, item.getId());
        assertEquals(name, item.getName());
        assertEquals(description, item.getDescription());
        assertEquals(available, item.getAvailable());
        assertEquals(owner.getId(), item.getOwner().getId());
    }

}
