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

import static java.lang.Thread.sleep;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

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

        // Assert
        assertEquals(1, bookings.size());
        assertTrue(bookings.stream().findFirst().isPresent());

        var firstBooking = bookings.stream().findFirst().get();
        assertThat(firstBooking.getId(), notNullValue());
        assertEquals(booking.getBooker().getId(), firstBooking.getBooker().getId());
        assertEquals(booking.getItem().getId(), firstBooking.getItem().getId());
        assertEquals(booking.getStatus(), firstBooking.getStatus());
    }

    @Test
    public void findAllByBookerIdAndStatusOrderByIdAsc_withValidSearchParams_returnsBookingCollection() {
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

        // Assert
        assertEquals(1, bookings.size());
        assertTrue(bookings.stream().findFirst().isPresent());

        var firstBooking = bookings.stream().findFirst().get();
        assertThat(firstBooking.getId(), notNullValue());
        assertEquals(booking.getBooker().getId(), firstBooking.getBooker().getId());
        assertEquals(booking.getItem().getId(), firstBooking.getItem().getId());
        assertEquals(booking.getStatus(), firstBooking.getStatus());
    }

    @Test
    public void findAllFutureBookingsByBookerIdOrderByStartDesc_withValidSearchParams_returnsBookingCollection() {
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

        // Assert
        assertEquals(1, bookings.size());
        assertTrue(bookings.stream().findFirst().isPresent());

        var firstBooking = bookings.stream().findFirst().get();
        assertThat(firstBooking.getId(), notNullValue());
        assertEquals(booking.getBooker().getId(), firstBooking.getBooker().getId());
        assertEquals(booking.getItem().getId(), firstBooking.getItem().getId());
        assertEquals(booking.getStatus(), firstBooking.getStatus());
    }

    @Test
    public void findAllPastBookingsByBookerIdOrderByStartDesc_withValidSearchParams_returnsBookingCollection() throws Exception {
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

        sleep(3000);

        // Act
        var bookings = bookingRepository.findAllPastBookingsByBookerIdOrderByStartDesc(booker.getId(),
                Pageable.unpaged()).getContent();

        // Assert
        assertEquals(1, bookings.size());
        assertTrue(bookings.stream().findFirst().isPresent());

        var firstBooking = bookings.stream().findFirst().get();
        assertThat(firstBooking.getId(), notNullValue());
        assertEquals(booking.getBooker().getId(), firstBooking.getBooker().getId());
        assertEquals(booking.getItem().getId(), firstBooking.getItem().getId());
        assertEquals(booking.getStatus(), firstBooking.getStatus());
    }

    @Test
    public void findAllCurrentBookingsByBookerIdOrderByIdAsc_withValidSearchParams_returnsBookingCollection()
            throws Exception {
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

        sleep(2000);

        // Act
        var bookings = bookingRepository.findAllCurrentBookingsByBookerIdOrderByIdAsc(booker.getId(),
                Pageable.unpaged()).getContent();

        // Assert
        assertEquals(1, bookings.size());
        assertTrue(bookings.stream().findFirst().isPresent());

        var firstBooking = bookings.stream().findFirst().get();
        assertThat(firstBooking.getId(), notNullValue());
        assertEquals(booking.getBooker().getId(), firstBooking.getBooker().getId());
        assertEquals(booking.getItem().getId(), firstBooking.getItem().getId());
        assertEquals(booking.getStatus(), firstBooking.getStatus());
    }

    @Test
    public void findAllByItemOwnerIdOrderByStartDesc_withValidSearchParams_returnsBookingCollection() {
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

        // Assert
        assertEquals(1, bookings.size());
        assertTrue(bookings.stream().findFirst().isPresent());

        var firstBooking = bookings.stream().findFirst().get();
        assertThat(firstBooking.getId(), notNullValue());
        assertEquals(booking.getBooker().getId(), firstBooking.getBooker().getId());
        assertEquals(booking.getItem().getId(), firstBooking.getItem().getId());
        assertEquals(booking.getStatus(), firstBooking.getStatus());
    }

    @Test
    public void findAllByItemOwnerIdAndStatusOrderByIdAsc_withValidSearchParams_returnsBookingCollection() {
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

        // Assert
        assertEquals(1, bookings.size());
        assertTrue(bookings.stream().findFirst().isPresent());

        var firstBooking = bookings.stream().findFirst().get();
        assertThat(firstBooking.getId(), notNullValue());
        assertEquals(booking.getBooker().getId(), firstBooking.getBooker().getId());
        assertEquals(booking.getItem().getId(), firstBooking.getItem().getId());
        assertEquals(booking.getStatus(), firstBooking.getStatus());
    }

    @Test
    public void findAllFutureBookingsByItemOwnerIdOrderByStartDesc_withValidSearchParams_returnsBookingCollection() {
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

        // Assert
        assertEquals(1, bookings.size());
        assertTrue(bookings.stream().findFirst().isPresent());

        var firstBooking = bookings.stream().findFirst().get();
        assertThat(firstBooking.getId(), notNullValue());
        assertEquals(booking.getBooker().getId(), firstBooking.getBooker().getId());
        assertEquals(booking.getItem().getId(), firstBooking.getItem().getId());
        assertEquals(booking.getStatus(), firstBooking.getStatus());
    }

    @Test
    public void findAllPastBookingsByItemOwnerIdOrderByStartDesc_withValidSearchParams_returnsBookingCollection()
            throws Exception {
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

        sleep(3000);

        // Act
        var bookings = bookingRepository.findAllPastBookingsByItemOwnerIdOrderByStartDesc(owner.getId(),
                Pageable.unpaged()).getContent();

        // Assert
        assertEquals(1, bookings.size());
        assertTrue(bookings.stream().findFirst().isPresent());

        var firstBooking = bookings.stream().findFirst().get();
        assertThat(firstBooking.getId(), notNullValue());
        assertEquals(booking.getBooker().getId(), firstBooking.getBooker().getId());
        assertEquals(booking.getItem().getId(), firstBooking.getItem().getId());
        assertEquals(booking.getStatus(), firstBooking.getStatus());
    }

    @Test
    public void findAllCurrentBookingsByItemOwnerIdOrderByIdAsc_withValidSearchParams_returnsBookingCollection()
            throws Exception {
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

        sleep(2000);

        // Act
        var bookings = bookingRepository.findAllCurrentBookingsByItemOwnerIdOrderByIdAsc(owner.getId(),
                Pageable.unpaged()).getContent();

        // Assert
        assertEquals(1, bookings.size());
        assertTrue(bookings.stream().findFirst().isPresent());

        var firstBooking = bookings.stream().findFirst().get();
        assertThat(firstBooking.getId(), notNullValue());
        assertEquals(booking.getBooker().getId(), firstBooking.getBooker().getId());
        assertEquals(booking.getItem().getId(), firstBooking.getItem().getId());
        assertEquals(booking.getStatus(), firstBooking.getStatus());
    }

    @Test
    public void findAllCurrentOrPastBookingsByItemIdOrderByStartAsc_withValidSearchParams_returnsBookingCollection()
            throws Exception {
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

        sleep(2000);

        // Act
        var bookings = bookingRepository.findAllCurrentOrPastBookingsByItemIdOrderByStartAsc(item.getId());

        // Assert
        assertEquals(1, bookings.size());
        assertTrue(bookings.stream().findFirst().isPresent());

        var firstBooking = bookings.stream().findFirst().get();
        assertThat(firstBooking.getId(), notNullValue());
        assertEquals(booking.getBooker().getId(), firstBooking.getBooker().getId());
        assertEquals(booking.getItem().getId(), firstBooking.getItem().getId());
        assertEquals(booking.getStatus(), firstBooking.getStatus());
    }

    @Test
    public void findAllFutureBookingsByItemIdOrderByStartDesc_withValidSearchParams_returnsBookingCollection() {
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

        // Assert
        assertEquals(1, bookings.size());
        assertTrue(bookings.stream().findFirst().isPresent());

        var firstBooking = bookings.stream().findFirst().get();
        assertThat(firstBooking.getId(), notNullValue());
        assertEquals(booking.getBooker().getId(), firstBooking.getBooker().getId());
        assertEquals(booking.getItem().getId(), firstBooking.getItem().getId());
        assertEquals(booking.getStatus(), firstBooking.getStatus());
    }

    @Test
    public void findAllPastBookingsByBookerIdAndItemIdOrderByIdAsc_withValidSearchParams_returnsBookingCollection()
            throws Exception {
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

        // Assert
        assertEquals(1, bookings.size());
        assertTrue(bookings.stream().findFirst().isPresent());

        var firstBooking = bookings.stream().findFirst().get();
        assertThat(firstBooking.getId(), notNullValue());
        assertEquals(booking.getBooker().getId(), firstBooking.getBooker().getId());
        assertEquals(booking.getItem().getId(), firstBooking.getItem().getId());
        assertEquals(booking.getStatus(), firstBooking.getStatus());
    }

}
