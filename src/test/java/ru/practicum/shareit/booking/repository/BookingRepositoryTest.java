package ru.practicum.shareit.booking.repository;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.jdbc.Sql;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.assertEquals;

@RequiredArgsConstructor(onConstructor_ = @Autowired)
@AutoConfigureTestDatabase
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@Sql(scripts = "classpath:schema.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@DataJpaTest
public class BookingRepositoryTest {

    private final BookingRepository bookingRepository;
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;

    @Test
    public void findAllByBookerIdOrderByStartDescTest() {
        // Arrange
        var owner = new User(1L,
                "Owner Name",
                "owner.name@mail.com");
        var booker = new User(2L,
                "Booker Name",
                "booker.name@mail.com");
        var item = new Item(
                1L,
                "Item Name",
                "Item Description",
                true,
                owner,
                null);
        var booking = new Booking(1L,
                LocalDateTime.now().plusDays(1),
                LocalDateTime.now().plusDays(2),
                new Item(
                        1L,
                        "Item Name",
                        "Item Description",
                        true,
                        new User(1L, "Owner Name", "owner.name@mail.com"),
                        null),
                new User(2L, "Booker Name", "booker.name@mail.com"),
                Status.WAITING);

        userRepository.save(owner);
        userRepository.save(booker);
        itemRepository.save(item);
        bookingRepository.save(booking);

        // Act
        var bookings = bookingRepository.findAllByBookerIdOrderByStartDesc(booker.getId(), Pageable.unpaged()).getContent();
        var firstBooking = bookings.stream().findFirst().get();

        // Assert
        assertEquals(1, bookings.size());
        assertThat(firstBooking.getId(), notNullValue());
        assertEquals(booking.getBooker().getId(), firstBooking.getBooker().getId());
        assertEquals(booking.getItem().getId(), firstBooking.getItem().getId());
        assertEquals(booking.getStatus(), firstBooking.getStatus());
    }

    @Test
    public void findAllByBookerIdAndStatusOrderByIdAscTest() {
        // Arrange
        var owner = new User(1L,
                "Owner Name",
                "owner.name@mail.com");
        var booker = new User(2L,
                "Booker Name",
                "booker.name@mail.com");
        var item = new Item(
                1L,
                "Item Name",
                "Item Description",
                true,
                owner,
                null);
        var booking = new Booking(1L,
                LocalDateTime.now().plusDays(1),
                LocalDateTime.now().plusDays(2),
                new Item(
                        1L,
                        "Item Name",
                        "Item Description",
                        true,
                        new User(1L, "Owner Name", "owner.name@mail.com"),
                        null),
                new User(2L, "Booker Name", "booker.name@mail.com"),
                Status.WAITING);

        userRepository.save(owner);
        userRepository.save(booker);
        itemRepository.save(item);
        bookingRepository.save(booking);

        // Act
        var bookings = bookingRepository.findAllByBookerIdAndStatusOrderByIdAsc(booker.getId(),
                Status.WAITING,
                Pageable.unpaged()).getContent();
        var firstBooking = bookings.stream().findFirst().get();

        // Assert
        assertEquals(1, bookings.size());
        assertThat(firstBooking.getId(), notNullValue());
        assertEquals(booking.getBooker().getId(), firstBooking.getBooker().getId());
        assertEquals(booking.getItem().getId(), firstBooking.getItem().getId());
        assertEquals(booking.getStatus(), firstBooking.getStatus());
    }

    @Test
    public void findAllFutureBookingsByBookerIdOrderByStartDescTest() {
        // Arrange
        var owner = new User(1L,
                "Owner Name",
                "owner.name@mail.com");
        var booker = new User(2L,
                "Booker Name",
                "booker.name@mail.com");
        var item = new Item(
                1L,
                "Item Name",
                "Item Description",
                true,
                owner,
                null);
        var booking = new Booking(1L,
                LocalDateTime.now().plusDays(1),
                LocalDateTime.now().plusDays(2),
                new Item(
                        1L,
                        "Item Name",
                        "Item Description",
                        true,
                        new User(1L, "Owner Name", "owner.name@mail.com"),
                        null),
                new User(2L, "Booker Name", "booker.name@mail.com"),
                Status.WAITING);

        userRepository.save(owner);
        userRepository.save(booker);
        itemRepository.save(item);
        bookingRepository.save(booking);

        // Act
        var bookings = bookingRepository.findAllFutureBookingsByBookerIdOrderByStartDesc(booker.getId(),
                Pageable.unpaged()).getContent();
        var firstBooking = bookings.stream().findFirst().get();

        // Assert
        assertEquals(1, bookings.size());
        assertThat(firstBooking.getId(), notNullValue());
        assertEquals(booking.getBooker().getId(), firstBooking.getBooker().getId());
        assertEquals(booking.getItem().getId(), firstBooking.getItem().getId());
        assertEquals(booking.getStatus(), firstBooking.getStatus());
    }

    @Test
    public void findAllPastBookingsByBookerIdOrderByStartDescTest() throws Exception {
        // Arrange
        var owner = new User(1L,
                "Owner Name",
                "owner.name@mail.com");
        var booker = new User(2L,
                "Booker Name",
                "booker.name@mail.com");
        var item = new Item(
                1L,
                "Item Name",
                "Item Description",
                true,
                owner,
                null);
        var booking = new Booking(1L,
                LocalDateTime.now().plusSeconds(1),
                LocalDateTime.now().plusSeconds(2),
                new Item(
                        1L,
                        "Item Name",
                        "Item Description",
                        true,
                        new User(1L, "Owner Name", "owner.name@mail.com"),
                        null),
                new User(2L, "Booker Name", "booker.name@mail.com"),
                Status.APPROVED);

        userRepository.save(owner);
        userRepository.save(booker);
        itemRepository.save(item);
        bookingRepository.save(booking);

        Thread.sleep(3000);

        // Act
        var bookings = bookingRepository.findAllPastBookingsByBookerIdOrderByStartDesc(booker.getId(),
                Pageable.unpaged()).getContent();
        var firstBooking = bookings.stream().findFirst().get();

        // Assert
        assertEquals(1, bookings.size());
        assertThat(firstBooking.getId(), notNullValue());
        assertEquals(booking.getBooker().getId(), firstBooking.getBooker().getId());
        assertEquals(booking.getItem().getId(), firstBooking.getItem().getId());
        assertEquals(booking.getStatus(), firstBooking.getStatus());
    }

    @Test
    public void findAllCurrentBookingsByBookerIdOrderByIdAscTest() throws Exception {
        // Arrange
        var owner = new User(1L,
                "Owner Name",
                "owner.name@mail.com");
        var booker = new User(2L,
                "Booker Name",
                "booker.name@mail.com");
        var item = new Item(
                1L,
                "Item Name",
                "Item Description",
                true,
                owner,
                null);
        var booking = new Booking(1L,
                LocalDateTime.now().plusSeconds(1),
                LocalDateTime.now().plusDays(2),
                new Item(
                        1L,
                        "Item Name",
                        "Item Description",
                        true,
                        new User(1L, "Owner Name", "owner.name@mail.com"),
                        null),
                new User(2L, "Booker Name", "booker.name@mail.com"),
                Status.APPROVED);

        userRepository.save(owner);
        userRepository.save(booker);
        itemRepository.save(item);
        bookingRepository.save(booking);

        Thread.sleep(2000);

        // Act
        var bookings = bookingRepository.findAllCurrentBookingsByBookerIdOrderByIdAsc(booker.getId(),
                Pageable.unpaged()).getContent();
        var firstBooking = bookings.stream().findFirst().get();

        // Assert
        assertEquals(1, bookings.size());
        assertThat(firstBooking.getId(), notNullValue());
        assertEquals(booking.getBooker().getId(), firstBooking.getBooker().getId());
        assertEquals(booking.getItem().getId(), firstBooking.getItem().getId());
        assertEquals(booking.getStatus(), firstBooking.getStatus());
    }

    @Test
    public void findAllByItemOwnerIdOrderByStartDescTest() {
        // Arrange
        var owner = new User(1L,
                "Owner Name",
                "owner.name@mail.com");
        var booker = new User(2L,
                "Booker Name",
                "booker.name@mail.com");
        var item = new Item(
                1L,
                "Item Name",
                "Item Description",
                true,
                owner,
                null);
        var booking = new Booking(1L,
                LocalDateTime.now().plusDays(1),
                LocalDateTime.now().plusDays(2),
                new Item(
                        1L,
                        "Item Name",
                        "Item Description",
                        true,
                        new User(1L, "Owner Name", "owner.name@mail.com"),
                        null),
                new User(2L, "Booker Name", "booker.name@mail.com"),
                Status.WAITING);

        userRepository.save(owner);
        userRepository.save(booker);
        itemRepository.save(item);
        bookingRepository.save(booking);

        // Act
        var bookings = bookingRepository.findAllByItemOwnerIdOrderByStartDesc(owner.getId(), Pageable.unpaged()).getContent();
        var firstBooking = bookings.stream().findFirst().get();

        // Assert
        assertEquals(1, bookings.size());
        assertThat(firstBooking.getId(), notNullValue());
        assertEquals(booking.getBooker().getId(), firstBooking.getBooker().getId());
        assertEquals(booking.getItem().getId(), firstBooking.getItem().getId());
        assertEquals(booking.getStatus(), firstBooking.getStatus());
    }

    @Test
    public void findAllByItemOwnerIdAndStatusOrderByIdAscTest() {
        // Arrange
        var owner = new User(1L,
                "Owner Name",
                "owner.name@mail.com");
        var booker = new User(2L,
                "Booker Name",
                "booker.name@mail.com");
        var item = new Item(
                1L,
                "Item Name",
                "Item Description",
                true,
                owner,
                null);
        var booking = new Booking(1L,
                LocalDateTime.now().plusDays(1),
                LocalDateTime.now().plusDays(2),
                new Item(
                        1L,
                        "Item Name",
                        "Item Description",
                        true,
                        new User(1L, "Owner Name", "owner.name@mail.com"),
                        null),
                new User(2L, "Booker Name", "booker.name@mail.com"),
                Status.WAITING);

        userRepository.save(owner);
        userRepository.save(booker);
        itemRepository.save(item);
        bookingRepository.save(booking);

        // Act
        var bookings = bookingRepository.findAllByItemOwnerIdAndStatusOrderByIdAsc(owner.getId(),
                Status.WAITING,
                Pageable.unpaged()).getContent();
        var firstBooking = bookings.stream().findFirst().get();

        // Assert
        assertEquals(1, bookings.size());
        assertThat(firstBooking.getId(), notNullValue());
        assertEquals(booking.getBooker().getId(), firstBooking.getBooker().getId());
        assertEquals(booking.getItem().getId(), firstBooking.getItem().getId());
        assertEquals(booking.getStatus(), firstBooking.getStatus());
    }

    @Test
    public void findAllFutureBookingsByItemOwnerIdOrderByStartDescTest() {
        // Arrange
        var owner = new User(1L,
                "Owner Name",
                "owner.name@mail.com");
        var booker = new User(2L,
                "Booker Name",
                "booker.name@mail.com");
        var item = new Item(
                1L,
                "Item Name",
                "Item Description",
                true,
                owner,
                null);
        var booking = new Booking(1L,
                LocalDateTime.now().plusDays(1),
                LocalDateTime.now().plusDays(2),
                new Item(
                        1L,
                        "Item Name",
                        "Item Description",
                        true,
                        new User(1L, "Owner Name", "owner.name@mail.com"),
                        null),
                new User(2L, "Booker Name", "booker.name@mail.com"),
                Status.WAITING);

        userRepository.save(owner);
        userRepository.save(booker);
        itemRepository.save(item);
        bookingRepository.save(booking);

        // Act
        var bookings = bookingRepository.findAllFutureBookingsByItemOwnerIdOrderByStartDesc(owner.getId(),
                Pageable.unpaged()).getContent();
        var firstBooking = bookings.stream().findFirst().get();

        // Assert
        assertEquals(1, bookings.size());
        assertThat(firstBooking.getId(), notNullValue());
        assertEquals(booking.getBooker().getId(), firstBooking.getBooker().getId());
        assertEquals(booking.getItem().getId(), firstBooking.getItem().getId());
        assertEquals(booking.getStatus(), firstBooking.getStatus());
    }

    @Test
    public void findAllPastBookingsByItemOwnerIdOrderByStartDescTest() throws Exception {
        // Arrange
        var owner = new User(1L,
                "Owner Name",
                "owner.name@mail.com");
        var booker = new User(2L,
                "Booker Name",
                "booker.name@mail.com");
        var item = new Item(
                1L,
                "Item Name",
                "Item Description",
                true,
                owner,
                null);
        var booking = new Booking(1L,
                LocalDateTime.now().plusSeconds(1),
                LocalDateTime.now().plusSeconds(2),
                new Item(
                        1L,
                        "Item Name",
                        "Item Description",
                        true,
                        new User(1L, "Owner Name", "owner.name@mail.com"),
                        null),
                new User(2L, "Booker Name", "booker.name@mail.com"),
                Status.APPROVED);

        userRepository.save(owner);
        userRepository.save(booker);
        itemRepository.save(item);
        bookingRepository.save(booking);

        Thread.sleep(3000);

        // Act
        var bookings = bookingRepository.findAllPastBookingsByItemOwnerIdOrderByStartDesc(owner.getId(),
                Pageable.unpaged()).getContent();
        var firstBooking = bookings.stream().findFirst().get();

        // Assert
        assertEquals(1, bookings.size());
        assertThat(firstBooking.getId(), notNullValue());
        assertEquals(booking.getBooker().getId(), firstBooking.getBooker().getId());
        assertEquals(booking.getItem().getId(), firstBooking.getItem().getId());
        assertEquals(booking.getStatus(), firstBooking.getStatus());
    }

    @Test
    public void findAllCurrentBookingsByItemOwnerIdOrderByIdAscTest() throws Exception {
        // Arrange
        var owner = new User(1L,
                "Owner Name",
                "owner.name@mail.com");
        var booker = new User(2L,
                "Booker Name",
                "booker.name@mail.com");
        var item = new Item(
                1L,
                "Item Name",
                "Item Description",
                true,
                owner,
                null);
        var booking = new Booking(1L,
                LocalDateTime.now().plusSeconds(1),
                LocalDateTime.now().plusDays(2),
                new Item(
                        1L,
                        "Item Name",
                        "Item Description",
                        true,
                        new User(1L, "Owner Name", "owner.name@mail.com"),
                        null),
                new User(2L, "Booker Name", "booker.name@mail.com"),
                Status.APPROVED);

        userRepository.save(owner);
        userRepository.save(booker);
        itemRepository.save(item);
        bookingRepository.save(booking);

        Thread.sleep(2000);

        // Act
        var bookings = bookingRepository.findAllCurrentBookingsByItemOwnerIdOrderByIdAsc(owner.getId(),
                Pageable.unpaged()).getContent();
        var firstBooking = bookings.stream().findFirst().get();

        // Assert
        assertEquals(1, bookings.size());
        assertThat(firstBooking.getId(), notNullValue());
        assertEquals(booking.getBooker().getId(), firstBooking.getBooker().getId());
        assertEquals(booking.getItem().getId(), firstBooking.getItem().getId());
        assertEquals(booking.getStatus(), firstBooking.getStatus());
    }

    @Test
    public void findAllCurrentOrPastBookingsByItemIdOrderByStartAscTest() throws Exception {
        // Arrange
        var owner = new User(1L,
                "Owner Name",
                "owner.name@mail.com");
        var booker = new User(2L,
                "Booker Name",
                "booker.name@mail.com");
        var item = new Item(
                1L,
                "Item Name",
                "Item Description",
                true,
                owner,
                null);
        var booking = new Booking(1L,
                LocalDateTime.now().plusSeconds(1),
                LocalDateTime.now().plusDays(2),
                new Item(
                        1L,
                        "Item Name",
                        "Item Description",
                        true,
                        new User(1L, "Owner Name", "owner.name@mail.com"),
                        null),
                new User(2L, "Booker Name", "booker.name@mail.com"),
                Status.APPROVED);

        userRepository.save(owner);
        userRepository.save(booker);
        itemRepository.save(item);
        bookingRepository.save(booking);

        Thread.sleep(2000);

        // Act
        var bookings = bookingRepository.findAllCurrentOrPastBookingsByItemIdOrderByStartAsc(item.getId());
        var firstBooking = bookings.stream().findFirst().get();

        // Assert
        assertEquals(1, bookings.size());
        assertThat(firstBooking.getId(), notNullValue());
        assertEquals(booking.getBooker().getId(), firstBooking.getBooker().getId());
        assertEquals(booking.getItem().getId(), firstBooking.getItem().getId());
        assertEquals(booking.getStatus(), firstBooking.getStatus());
    }

    @Test
    public void findAllFutureBookingsByItemIdOrderByStartDescTest() {
        // Arrange
        var owner = new User(1L,
                "Owner Name",
                "owner.name@mail.com");
        var booker = new User(2L,
                "Booker Name",
                "booker.name@mail.com");
        var item = new Item(
                1L,
                "Item Name",
                "Item Description",
                true,
                owner,
                null);
        var booking = new Booking(1L,
                LocalDateTime.now().plusDays(1),
                LocalDateTime.now().plusDays(2),
                new Item(
                        1L,
                        "Item Name",
                        "Item Description",
                        true,
                        new User(1L, "Owner Name", "owner.name@mail.com"),
                        null),
                new User(2L, "Booker Name", "booker.name@mail.com"),
                Status.WAITING);

        userRepository.save(owner);
        userRepository.save(booker);
        itemRepository.save(item);
        bookingRepository.save(booking);

        // Act
        var bookings = bookingRepository.findAllFutureBookingsByItemIdOrderByStartDesc(item.getId());
        var firstBooking = bookings.stream().findFirst().get();

        // Assert
        assertEquals(1, bookings.size());
        assertThat(firstBooking.getId(), notNullValue());
        assertEquals(booking.getBooker().getId(), firstBooking.getBooker().getId());
        assertEquals(booking.getItem().getId(), firstBooking.getItem().getId());
        assertEquals(booking.getStatus(), firstBooking.getStatus());
    }

    @Test
    public void findAllPastBookingsByBookerIdAndItemIdOrderByIdAscTest() throws Exception {
        // Arrange
        var owner = new User(1L,
                "Owner Name",
                "owner.name@mail.com");
        var booker = new User(2L,
                "Booker Name",
                "booker.name@mail.com");
        var item = new Item(
                1L,
                "Item Name",
                "Item Description",
                true,
                owner,
                null);
        var booking = new Booking(1L,
                LocalDateTime.now().plusSeconds(1),
                LocalDateTime.now().plusSeconds(2),
                new Item(
                        1L,
                        "Item Name",
                        "Item Description",
                        true,
                        new User(1L, "Owner Name", "owner.name@mail.com"),
                        null),
                new User(2L, "Booker Name", "booker.name@mail.com"),
                Status.APPROVED);

        userRepository.save(owner);
        userRepository.save(booker);
        itemRepository.save(item);
        bookingRepository.save(booking);

        Thread.sleep(3000);

        // Act
        var bookings = bookingRepository.findAllPastBookingsByBookerIdAndItemIdOrderByIdAsc(booker.getId(),
                item.getId());
        var firstBooking = bookings.stream().findFirst().get();

        // Assert
        assertEquals(1, bookings.size());
        assertThat(firstBooking.getId(), notNullValue());
        assertEquals(booking.getBooker().getId(), firstBooking.getBooker().getId());
        assertEquals(booking.getItem().getId(), firstBooking.getItem().getId());
        assertEquals(booking.getStatus(), firstBooking.getStatus());
    }

}
