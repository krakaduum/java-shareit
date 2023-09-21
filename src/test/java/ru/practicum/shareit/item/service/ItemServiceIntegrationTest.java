package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.jdbc.Sql;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.service.UserService;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.assertEquals;

@RequiredArgsConstructor(onConstructor_ = @Autowired)
@AutoConfigureTestDatabase
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@Sql(scripts = "classpath:schema.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@SpringBootTest
public class ItemServiceIntegrationTest {

    private final UserService userService;
    private final ItemService itemService;

    @Test
    public void addItemTest() {
        // Arrange
        var userDto = new UserDto(1L,
                "User Name",
                "user.name@mail.com");
        var itemDto = new ItemDto(
                1L,
                "Item Name",
                "Item Description",
                true,
                1L,
                null);

        userService.addUser(userDto);

        // Act
        var addedItemDto = itemService.addItem(1L, itemDto);

        // Assert
        assertThat(addedItemDto.getId(), notNullValue());
        assertEquals(itemDto.getName(), addedItemDto.getName());
        assertEquals(itemDto.getDescription(), addedItemDto.getDescription());
        assertEquals(itemDto.getAvailable(), addedItemDto.getAvailable());
        assertEquals(itemDto.getOwnerId(), addedItemDto.getOwnerId());
        assertEquals(itemDto.getRequestId(), addedItemDto.getRequestId());
    }

}
