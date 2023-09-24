package ru.practicum.shareit.booking.service;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.jdbc.Sql;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingRequestBody;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.service.UserService;

import java.time.LocalDateTime;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.assertEquals;

@RequiredArgsConstructor(onConstructor_ = @Autowired)
@AutoConfigureTestDatabase
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@Sql(scripts = "classpath:schema.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@SpringBootTest
public class BookingServiceIntegrationTest {

    private final UserService userService;
    private final ItemService itemService;
    private final BookingService bookingService;

    @Test
    public void addBooking_withValidData_returnsBookingDto() {
        // Arrange
        var bookingRequestBody = new BookingRequestBody(1L,
                LocalDateTime.now().plusDays(1),
                LocalDateTime.now().plusDays(2));
        var ownerDto = new UserDto(1L,
                "Owner Name",
                "owner.name@mail.com");
        var bookerDto = new UserDto(2L,
                "Booker Name",
                "booker.name@mail.com");
        var itemDto = new ItemDto(
                1L,
                "Item Name",
                "Item Description",
                true,
                1L,
                null);
        var bookingDto = new BookingDto(1L,
                LocalDateTime.now().plusDays(1),
                LocalDateTime.now().plusDays(2),
                new ItemDto(
                        1L,
                        "Item Name",
                        "Item Description",
                        true,
                        1L,
                        1L),
                new UserDto(2L, "Booker Name", "booker.name@mail.com"),
                Status.WAITING);

        userService.addUser(bookerDto);
        userService.addUser(ownerDto);
        itemService.addItem(ownerDto.getId(), itemDto);

        // Act
        var addedBookingDto = bookingService.addBooking(2L, bookingRequestBody);

        // Assert
        assertThat(addedBookingDto.getId(), notNullValue());
        assertEquals(bookingDto.getBooker().getId(), addedBookingDto.getBooker().getId());
        assertEquals(bookingDto.getItem().getId(), addedBookingDto.getItem().getId());
        assertEquals(bookingDto.getStatus(), addedBookingDto.getStatus());
    }

}
