package ru.practicum.shareit.booking.model;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class BookingTest {

    @Test
    public void constructor_withValidData_returnsBooking() {
        // Arrange
        long id = 1L;
        LocalDateTime start = LocalDateTime.now();
        LocalDateTime end = LocalDateTime.now().plusSeconds(5);
        var item = new Item(1L,
                "Item Name",
                "Item Description",
                true,
                new User(1L, "Owner Name", "owner.name@mail.com"),
                null);
        var booker = new User(2L, "Booker Name", "booker.name@mail.com");
        Status status = Status.WAITING;

        // Act
        var booking = new Booking(id, start, end, item, booker, status);

        // Assert
        assertEquals(id, booking.getId());
        assertEquals(start, booking.getStart());
        assertEquals(end, booking.getEnd());
        assertEquals(item.getId(), booking.getItem().getId());
        assertEquals(booker.getId(), booking.getBooker().getId());
        assertEquals(status, booking.getStatus());
    }

}
