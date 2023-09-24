package ru.practicum.shareit.item.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageImpl;
import ru.practicum.shareit.booking.dto.BookingShortDto;
import ru.practicum.shareit.booking.exception.AccessDeniedException;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.CommentRequestBody;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemExtendedDto;
import ru.practicum.shareit.item.exception.InvalidAuthorException;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.CommentRepository;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.repository.ItemRequestRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import javax.validation.ValidationException;
import java.time.LocalDateTime;
import java.util.*;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.openMocks;

@SpringBootTest
public class ItemServiceTest {

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
    public void addItem_withValidData_returnsItemDto() throws ValidationException, NoSuchElementException {
        // Arrange
        var owner = new User(1L, "Owner Name", "owner.name@mail.com");
        var item = new Item(1L,
                "Item Name",
                "Item Description",
                true,
                owner,
                null);
        var itemDto = new ItemDto(
                1L,
                "Item Name",
                "Item Description",
                true,
                1L,
                null);

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
    public void addItem_withValidDataWithRequestId_returnsItemDtoWithRequest()
            throws ValidationException, NoSuchElementException {
        // Arrange
        var owner = new User(1L, "Owner Name", "owner.name@mail.com");
        var requester = new User(2L, "Requester Name", "requester.name@mail.com");
        var item = new Item(
                1L,
                "Item Name",
                "Item Description",
                true,
                owner,
                null);
        var itemRequest = new ItemRequest(1L, "Description", requester, LocalDateTime.now(), Set.of(item));
        item.setRequest(itemRequest);
        var itemDto = new ItemDto(
                1L,
                "Item Name",
                "Item Description",
                true,
                1L,
                1L);

        when(userRepository.findById(any()))
                .thenReturn(Optional.of(owner));
        when(itemRequestRepository.findById(any()))
                .thenReturn(Optional.of(itemRequest));
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
    public void addItem_withEmptyName_throwsValidationException() throws ValidationException {
        // Arrange
        var itemDto = new ItemDto(
                1L,
                "",
                "Item Description",
                true,
                1L,
                null);

        // Act & Assert
        assertThrows(ValidationException.class, () -> itemService.addItem(1L, itemDto));
    }

    @Test
    public void addItem_withEmptyDescription_throwsValidationException() throws ValidationException {
        // Arrange
        var itemDto = new ItemDto(
                1L,
                "Item Name",
                "",
                true,
                1L,
                null);

        // Act & Assert
        assertThrows(ValidationException.class, () -> itemService.addItem(1L, itemDto));
    }

    @Test
    public void addItem_withEmptyAvailable_throwsValidationException() throws ValidationException {
        // Arrange
        var itemDto = new ItemDto(
                1L,
                "Item Name",
                "Item Description",
                null,
                1L,
                null);

        // Act & Assert
        assertThrows(ValidationException.class, () -> itemService.addItem(1L, itemDto));
    }

    @Test
    public void addItem_withInvalidOwnerId_throwsNoSuchElementException() throws NoSuchElementException {
        // Arrange
        var itemDto = new ItemDto(
                1L,
                "Item Name",
                "Item Description",
                true,
                1L,
                null);

        // Act & Assert
        assertThrows(NoSuchElementException.class, () -> itemService.addItem(1L, itemDto));
    }

    @Test
    public void getItem_withValidData_returnsItemDto() throws NoSuchElementException {
        // Arrange
        var owner = new User(1L,
                "Owner Name",
                "owner.name@mail.com");
        var item = new Item(
                1L,
                "Item Name",
                "Item Description",
                true,
                owner,
                null);
        var itemExtendedDto = new ItemExtendedDto(
                1L,
                "Item Name",
                "Item Description",
                true,
                1L,
                new BookingShortDto(2L, 2L),
                new BookingShortDto(3L, 3L),
                List.of(new CommentDto(1L, "Comment", "Author Name", LocalDateTime.now())));

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
    public void getItem_withInvalidItemId_throwsNoSuchElementException() throws NoSuchElementException {
        // Act & Assert
        assertThrows(NoSuchElementException.class, () -> itemService.getItem(1L, 1L));
    }

    @Test
    public void updateItem_withValidData_returnsItemDto() throws NoSuchElementException, ValidationException {
        // Arrange
        var owner = new User(1L,
                "Owner Name",
                "owner.name@mail.com");
        var item = new Item(
                1L,
                "Item Name",
                "Item Description",
                true,
                owner,
                null);
        var itemDto = new ItemDto(
                1L,
                "Item Name",
                "Item Description",
                true,
                1L,
                null);

        when(itemRepository.findById(anyLong()))
                .thenReturn(Optional.of(item));
        when(userRepository.findById(any()))
                .thenReturn(Optional.of(owner));
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
    public void updateItem_withInvalidItemId_throwsNoSuchElementException() throws NoSuchElementException {
        // Act & Assert
        assertThrows(NoSuchElementException.class, () -> itemService.updateItem(1L, 1L, null));
    }

    @Test
    public void updateItem_withoutOwnerInDatabase_throwsNoSuchElementException() throws NoSuchElementException {
        // Arrange
        var owner = new User(1L,
                "Owner Name",
                "owner.name@mail.com");
        var item = new Item(
                1L,
                "Item Name",
                "Item Description",
                true,
                owner,
                null);

        when(itemRepository.findById(anyLong()))
                .thenReturn(Optional.of(item));

        // Act & Assert
        assertThrows(NoSuchElementException.class, () -> itemService.updateItem(2L, 1L, null));
    }

    @Test
    public void updateItem_withInvalidOwnerId_throwsNoSuchElementException() throws NoSuchElementException {
        // Arrange
        var owner = new User(1L,
                "Owner Name",
                "owner.name@mail.com");
        var owner2 = new User(2L,
                "Owner2 Name",
                "owner2.name@mail.com");
        var item = new Item(
                1L,
                "Item Name",
                "Item Description",
                true,
                owner2,
                null);

        when(itemRepository.findById(anyLong()))
                .thenReturn(Optional.of(item));
        when(userRepository.findById(any()))
                .thenReturn(Optional.of(owner));

        // Act & Assert
        assertThrows(NoSuchElementException.class, () -> itemService.updateItem(1L, 1L, null));
    }

    @Test
    public void removeItem_withValidData_callsItemRepositoryTwice()
            throws NoSuchElementException, AccessDeniedException {
        // Arrange
        var owner = new User(1L, "Owner Name", "owner.name@mail.com");
        var item = new Item(1L,
                "Item Name",
                "Item Description",
                true,
                owner,
                null);

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
    public void removeItem_withInvalidItemId_throwsNoSuchElementException() throws NoSuchElementException {
        // Act & Assert
        assertThrows(NoSuchElementException.class, () -> itemService.removeItem(1L, 1L));
    }

    @Test
    public void removeItem_withInvalidOwnerId_throwsAccessDeniedException() throws AccessDeniedException {
        // Arrange
        var owner = new User(1L, "Owner Name", "owner.name@mail.com");
        var item = new Item(1L,
                "Item Name",
                "Item Description",
                true,
                owner,
                null);

        when(itemRepository.findById(anyLong()))
                .thenReturn(Optional.of(item));

        // Act & Assert
        assertThrows(AccessDeniedException.class, () -> itemService.removeItem(2L, 1L));
    }

    @Test
    public void getItemsByOwnerId_withValidData_returnsItemCollection() throws IllegalArgumentException {
        // Arrange
        var owner = new User(1L,
                "Owner Name",
                "owner.name@mail.com");
        var item = new Item(
                1L,
                "Item Name",
                "Item Description",
                true,
                owner,
                null);
        var itemExtendedDto = new ItemExtendedDto(
                1L,
                "Item Name",
                "Item Description",
                true,
                1L,
                new BookingShortDto(2L, 2L),
                new BookingShortDto(3L, 3L),
                List.of(new CommentDto(1L, "Comment", "Author Name", LocalDateTime.now())));

        when(itemRepository.findAllByOwnerIdOrderByIdAsc(anyLong(), any()))
                .thenReturn(new PageImpl<>(List.of(item)));

        // Act
        var items = itemService.getItemsByOwnerId(1L, null, null);
        var firstItem = items.stream().findFirst();

        // Assert
        assertEquals(items.size(), 1);
        assertTrue(firstItem.isPresent());
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
    public void getItemsByOwnerId_withInvalidSearchParams_throwsIllegalArgumentException()
            throws IllegalArgumentException {
        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> itemService.getItemsByOwnerId(1L, -1, -1));
    }

    @Test
    public void searchItems_withValidData_returnsItemCollection() throws IllegalArgumentException {
        // Arrange
        var owner = new User(1L,
                "Owner Name",
                "owner.name@mail.com");
        var item = new Item(
                1L,
                "Item Name",
                "Item Description",
                true,
                owner,
                null);
        var itemDto = new ItemDto(
                1L,
                "Item Name",
                "Item Description",
                true,
                1L,
                null);

        when(itemRepository.findAllAvailableItemsByNameOrDescription(anyString(), any()))
                .thenReturn(new PageImpl<>(List.of(item)));

        // Act
        var items = itemService.searchItems("query", null, null);
        var firstItem = items.stream().findFirst();

        // Assert
        assertEquals(1, items.size());
        assertTrue(firstItem.isPresent());
        assertThat(firstItem.get().getId(), notNullValue());
        assertEquals(itemDto.getName(), firstItem.get().getName());
        assertEquals(itemDto.getDescription(), firstItem.get().getDescription());
        assertEquals(itemDto.getAvailable(), firstItem.get().getAvailable());
        assertEquals(itemDto.getOwnerId(), firstItem.get().getOwnerId());
        assertEquals(itemDto.getRequestId(), firstItem.get().getRequestId());
    }

    @Test
    public void searchItems_withInvalidSearchParams_throwsIllegalArgumentException()
            throws IllegalArgumentException {
        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> itemService.searchItems("query", -1, -1));
    }

    @Test
    public void addComment_withValidData_returnsCommentDto()
            throws NoSuchElementException, InvalidAuthorException, ValidationException {
        // Arrange
        var owner = new User(1L,
                "Owner Name",
                "owner.name@mail.com");
        var author = new User(2L,
                "Author Name",
                "author.name@mail.com");
        var item = new Item(
                1L,
                "Item Name",
                "Item Description",
                true,
                owner,
                null);
        var commentRequestBody = new CommentRequestBody("Text");
        var comment = new Comment(2L,
                "Text",
                item,
                author,
                LocalDateTime.now());

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
        assertEquals(comment.getText(), addedCommentDto.getText());
        assertEquals(comment.getAuthor().getName(), addedCommentDto.getAuthorName());
    }

    @Test
    public void addComment_withInvalidAuthorId_throwsNoSuchElementException() throws NoSuchElementException {
        // Arrange
        var commentRequestBody = new CommentRequestBody("Text");

        // Act & Assert
        assertThrows(NoSuchElementException.class, () -> itemService.addComment(1L, 1L, commentRequestBody));
    }

    @Test
    public void addComment_withInvalidItemId_throwsNoSuchElementException() throws NoSuchElementException {
        // Arrange
        var author = new User(2L,
                "Author Name",
                "author.name@mail.com");
        var commentRequestBody = new CommentRequestBody("Text");

        when(userRepository.findById(any()))
                .thenReturn(Optional.of(author));

        // Act & Assert
        assertThrows(NoSuchElementException.class, () -> itemService.addComment(1L, 1L, commentRequestBody));
    }

    @Test
    public void addComment_withValidAuthorWithoutBookings_throwsInvalidAuthorException() throws InvalidAuthorException {
        // Arrange
        var owner = new User(1L,
                "Owner Name",
                "owner.name@mail.com");
        var author = new User(2L,
                "Author Name",
                "author.name@mail.com");
        var item = new Item(
                1L,
                "Item Name",
                "Item Description",
                true,
                owner,
                null);
        var commentRequestBody = new CommentRequestBody("Text");

        when(userRepository.findById(any()))
                .thenReturn(Optional.of(author));
        when(itemRepository.findById(any()))
                .thenReturn(Optional.of(item));

        // Act & Assert
        assertThrows(InvalidAuthorException.class, () -> itemService.addComment(1L, 1L, commentRequestBody));
    }

    @Test
    public void addComment_withInvalidText_throwsValidationException() throws ValidationException {
        // Arrange
        var owner = new User(1L,
                "Owner Name",
                "owner.name@mail.com");
        var author = new User(2L,
                "Author Name",
                "author.name@mail.com");
        var item = new Item(
                1L,
                "Item Name",
                "Item Description",
                true,
                owner,
                null);
        var commentRequestBody = new CommentRequestBody("");

        when(userRepository.findById(any()))
                .thenReturn(Optional.of(author));
        when(itemRepository.findById(any()))
                .thenReturn(Optional.of(item));
        when(bookingRepository.findAllPastBookingsByBookerIdAndItemIdOrderByIdAsc(anyLong(), anyLong()))
                .thenReturn(List.of(new Booking()));

        // Act & Assert
        assertThrows(ValidationException.class, () -> itemService.addComment(1L, 1L, commentRequestBody));
    }

}
