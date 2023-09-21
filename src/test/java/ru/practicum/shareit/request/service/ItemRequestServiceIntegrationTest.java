package ru.practicum.shareit.request.service;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.jdbc.Sql;
import ru.practicum.shareit.request.dto.ItemRequestRequestBody;
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
public class ItemRequestServiceIntegrationTest {

    private final ItemRequestService itemRequestService;
    private final UserService userService;

    @Test
    public void addItemRequest_withValidData_returnsItemRequestDto() {
        // Arrange
        var itemRequestRequestBody = new ItemRequestRequestBody("Description");
        var requesterDto = new UserDto(2L,
                "Requester Name",
                "requester.name@mail.com");

        userService.addUser(requesterDto);

        // Act
        var addedItemRequestDto = itemRequestService.addItemRequest(1L, itemRequestRequestBody);

        // Assert
        assertThat(addedItemRequestDto.getId(), notNullValue());
        assertEquals(itemRequestRequestBody.getDescription(), addedItemRequestDto.getDescription());
    }

}
