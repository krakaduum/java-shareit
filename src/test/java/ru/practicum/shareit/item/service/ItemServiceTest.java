package ru.practicum.shareit.item.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageImpl;
import ru.practicum.shareit.booking.dto.BookingShortDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.CommentRequestBody;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemExtendedDto;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.CommentRepository;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.request.repository.ItemRequestRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.openMocks;

@SpringBootTest
public class ItemServiceTest {

    private final Item item = new Item(
            1L,
            "Item Name",
            "Item Description",
            true,
            new User(1L, "User Name", "user.name@mail.com"),
            null);

    private final ItemDto itemDto = new ItemDto(
            1L,
            "Item Name",
            "Item Description",
            true,
            1L,
            null);

    private final ItemExtendedDto itemExtendedDto = new ItemExtendedDto(
            1L,
            "Item Name",
            "Item Description",
            true,
            1L,
            new BookingShortDto(2L, 2L),
            new BookingShortDto(3L, 3L),
            List.of(new CommentDto(1L, "Comment", "Author Name", LocalDateTime.now())));

    private final Comment comment = new Comment(2L,
            "Text",
            null,
            new User(2L,
                    "Author Name",
                    "author.name@mail.com"),
            LocalDateTime.now());

    private final CommentRequestBody commentRequestBody = new CommentRequestBody("Text");

    private final CommentDto commentDto = new CommentDto(
            1L,
            "Text",
            "Author Name",
            LocalDateTime.now());

    private final User owner = new User(1L,
            "Owner Name",
            "owner.name@mail.com");

    private final User author = new User(2L,
            "Author Name",
            "author.name@mail.com");

    @Mock
    private ItemRepository itemRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private BookingRepository bookingRepository;
    @Mock
    private CommentRepository commentRepository;
    @Mock
    private ItemRequestRepository itemRequestRepository;

    private ItemService itemService;

    @BeforeEach
    public void setUp() {
        openMocks(this);
        itemService = new ItemServiceImpl(
                itemRepository,
                userRepository,
                bookingRepository,
                commentRepository,
                itemRequestRepository);
    }

    @Test
    public void addItemTest() {
        // Arrange
        when(userRepository.findById(any()))
                .thenReturn(Optional.of(owner));
        when(itemRepository.save(any()))
                .thenReturn(item);

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

    @Test
    public void getItemTest() {
        // Arrange
        when(userRepository.findById(any()))
                .thenReturn(Optional.of(owner));
        when(itemRepository.findById(anyLong()))
                .thenReturn(Optional.of(item));
        when(bookingRepository.findAllCurrentOrPastBookingsByItemIdOrderByStartAsc(anyLong()))
                .thenReturn(Collections.emptyList());
        when(bookingRepository.findAllFutureBookingsByItemIdOrderByStartDesc(anyLong()))
                .thenReturn(Collections.emptyList());
        when(commentRepository.findAllByItemIdOrderByIdAsc(anyLong()))
                .thenReturn(Collections.emptyList());

        // Act
        var foundItemDto = itemService.getItem(1L, 1L);

        // Assert
        assertThat(foundItemDto.getId(), notNullValue());
        assertEquals(itemExtendedDto.getName(), foundItemDto.getName());
        assertEquals(itemExtendedDto.getDescription(), foundItemDto.getDescription());
        assertEquals(itemExtendedDto.isAvailable(), foundItemDto.isAvailable());
        assertEquals(itemExtendedDto.getOwnerId(), foundItemDto.getOwnerId());
        assertNull(foundItemDto.getLastBooking());
        assertNull(foundItemDto.getNextBooking());
        assertEquals(0, foundItemDto.getComments().size());
    }

    @Test
    public void updateItemTest() {
        // Arrange
        when(userRepository.findById(any()))
                .thenReturn(Optional.of(owner));
        when(itemRepository.findById(anyLong()))
                .thenReturn(Optional.of(item));
        when(itemRepository.save(any()))
                .thenReturn(item);

        // Act
        var updatedItemDto = itemService.updateItem(1L, 1L, itemDto);

        // Assert
        assertThat(updatedItemDto.getId(), notNullValue());
        assertEquals(itemDto.getName(), updatedItemDto.getName());
        assertEquals(itemDto.getDescription(), updatedItemDto.getDescription());
        assertEquals(itemDto.getAvailable(), updatedItemDto.getAvailable());
        assertEquals(itemDto.getOwnerId(), updatedItemDto.getOwnerId());
        assertEquals(itemDto.getRequestId(), updatedItemDto.getRequestId());
    }

    @Test
    public void removeItemTest() {
        // Arrange
        when(itemRepository.findById(anyLong()))
                .thenReturn(Optional.of(item));

        // Act
        itemService.removeItem(1L, 1L);

        // Assert
        verify(itemRepository, times(1))
                .findById(anyLong());
        verify(itemRepository, times(1))
                .deleteById(anyLong());
    }

    @Test
    public void getItemsByOwnerIdTest() {
        // Arrange
        when(itemRepository.findAllByOwnerIdOrderByIdAsc(anyLong(), any()))
                .thenReturn(new PageImpl(List.of(item)));

        // Act
        var items = itemService.getItemsByOwnerId(1L, null, null);
        var firstItem = items.stream().findFirst();

        // Assert
        assertEquals(items.size(), 1);
        assertThat(firstItem.get().getId(), notNullValue());
        assertEquals(itemExtendedDto.getName(), firstItem.get().getName());
        assertEquals(itemExtendedDto.getDescription(), firstItem.get().getDescription());
        assertEquals(itemExtendedDto.isAvailable(), firstItem.get().isAvailable());
        assertEquals(itemExtendedDto.getOwnerId(), firstItem.get().getOwnerId());
        assertNull(firstItem.get().getLastBooking());
        assertNull(firstItem.get().getNextBooking());
        assertEquals(0, firstItem.get().getComments().size());
    }

    @Test
    public void searchItemsTest() {
        // Arrange
        when(itemRepository.findAllAvailableItemsByNameOrDescription(anyString(), any()))
                .thenReturn(new PageImpl(List.of(item)));

        // Act
        var items = itemService.searchItems("query", null, null);
        var firstItem = items.stream().findFirst();

        // Assert
        assertEquals(1, items.size());
        assertThat(firstItem.get().getId(), notNullValue());
        assertEquals(itemDto.getName(), firstItem.get().getName());
        assertEquals(itemDto.getDescription(), firstItem.get().getDescription());
        assertEquals(itemDto.getAvailable(), firstItem.get().getAvailable());
        assertEquals(itemDto.getOwnerId(), firstItem.get().getOwnerId());
        assertEquals(itemDto.getRequestId(), firstItem.get().getRequestId());
    }

    @Test
    public void addCommentTest() {
        // Arrange
        when(userRepository.findById(any()))
                .thenReturn(Optional.of(author));
        when(itemRepository.findById(any()))
                .thenReturn(Optional.of(item));
        when(bookingRepository.findAllPastBookingsByBookerIdAndItemIdOrderByIdAsc(anyLong(), anyLong()))
                .thenReturn(List.of(new Booking()));
        when(commentRepository.save(any()))
                .thenReturn(comment);

        // Act
        var addedCommentDto = itemService.addComment(2L, 1L, commentRequestBody);

        // Assert
        assertThat(addedCommentDto.getId(), notNullValue());
        assertEquals(commentDto.getText(), addedCommentDto.getText());
        assertEquals(commentDto.getAuthorName(), addedCommentDto.getAuthorName());
    }

}
