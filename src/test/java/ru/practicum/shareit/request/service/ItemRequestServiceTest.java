package ru.practicum.shareit.request.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageImpl;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.dto.ItemRequestRequestBody;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.repository.ItemRequestRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import javax.validation.ValidationException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Set;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.openMocks;

@SpringBootTest
public class ItemRequestServiceTest {

    @Mock
    private ItemRequestRepository itemRequestRepository;
    @Mock
    private UserRepository userRepository;

    private ItemRequestService itemRequestService;

    @BeforeEach
    public void setUp() {
        openMocks(this);
        itemRequestService = new ItemRequestServiceImpl(itemRequestRepository, userRepository);
    }

    @Test
    public void addItemRequest_withValidData_returnsItemRequestDto() throws NoSuchElementException, ValidationException {
        // Arrange
        var requester = new User(3L,
                "Requester Name",
                "requester.name@mail.com");
        var itemRequestRequestBody = new ItemRequestRequestBody("Description");
        var itemRequest = new ItemRequest(1L,
                "Description",
                requester,
                LocalDateTime.now(),
                Set.of(new Item(
                        1L,
                        "Item Name",
                        "Item Description",
                        true,
                        new User(1L, "Owner Name", "owner.name@mail.com"),
                        null)));

        when(userRepository.findById(any()))
                .thenReturn(Optional.of(requester));
        when(itemRequestRepository.save(any()))
                .thenReturn(itemRequest);

        // Act
        var addedItemRequestDto = itemRequestService.addItemRequest(3L, itemRequestRequestBody);

        // Assert
        assertThat(addedItemRequestDto.getId(), notNullValue());
        assertEquals(itemRequest.getDescription(), addedItemRequestDto.getDescription());
        assertEquals(itemRequest.getItems().stream().findFirst().get().getId(),
                addedItemRequestDto.getItems().stream().findFirst().get().getId());
    }

    @Test
    public void addItemRequest_withInvalidRequesterId_throwsNoSuchElementException() throws NoSuchElementException {
        // Arrange
        var itemRequestRequestBody = new ItemRequestRequestBody("Description");

        // Act & Assert
        assertThrows(NoSuchElementException.class, () -> itemRequestService.addItemRequest(3L,
                itemRequestRequestBody));
    }

    @Test
    public void addItemRequest_withInvalidDescription_throwsValidationException() throws ValidationException {
        // Arrange
        var requester = new User(3L,
                "Requester Name",
                "requester.name@mail.com");
        var itemRequestRequestBody = new ItemRequestRequestBody("");

        when(userRepository.findById(any()))
                .thenReturn(Optional.of(requester));

        // Act & Assert
        assertThrows(ValidationException.class, () -> itemRequestService.addItemRequest(3L,
                itemRequestRequestBody));
    }

    @Test
    public void getItemRequest_withValidData_returnsItemRequestDto() throws NoSuchElementException {
        // Arrange
        var requester = new User(3L,
                "Requester Name",
                "requester.name@mail.com");
        var itemRequest = new ItemRequest(1L,
                "Description",
                requester,
                LocalDateTime.now(),
                Set.of(new Item(
                        1L,
                        "Item Name",
                        "Item Description",
                        true,
                        new User(1L, "Owner Name", "owner.name@mail.com"),
                        null)));

        when(userRepository.findById(any()))
                .thenReturn(Optional.of(requester));
        when(itemRequestRepository.findById(any()))
                .thenReturn(Optional.of(itemRequest));

        // Act
        var foundItemRequestDto = itemRequestService.getItemRequest(3L, 1L);

        // Assert
        assertThat(foundItemRequestDto.getId(), notNullValue());
        assertEquals(itemRequest.getDescription(), foundItemRequestDto.getDescription());
        assertEquals(itemRequest.getItems().stream().findFirst().get().getId(),
                foundItemRequestDto.getItems().stream().findFirst().get().getId());
    }

    @Test
    public void getItemRequest_withInvalidRequesterId_throwsNoSuchElementException() throws NoSuchElementException {
        // Act & Assert
        assertThrows(NoSuchElementException.class, () -> itemRequestService.getItemRequest(3L, 1L));
    }

    @Test
    public void getItemRequest_withInvalidItemRequestId_throwsNoSuchElementException() throws NoSuchElementException {
        // Arrange
        var requester = new User(3L,
                "Requester Name",
                "requester.name@mail.com");

        when(userRepository.findById(any()))
                .thenReturn(Optional.of(requester));

        // Act & Assert
        assertThrows(NoSuchElementException.class, () -> itemRequestService.getItemRequest(3L, 1L));
    }

    @Test
    public void getItemRequestsByRequesterId_withValidData_returnsItemRequestCollection() throws NoSuchElementException {
        // Arrange
        var requester = new User(3L,
                "Requester Name",
                "requester.name@mail.com");
        var itemRequest = new ItemRequest(1L,
                "Description",
                requester,
                LocalDateTime.now(),
                Set.of(new Item(
                        1L,
                        "Item Name",
                        "Item Description",
                        true,
                        new User(1L, "Owner Name", "owner.name@mail.com"),
                        null)));

        when(userRepository.findById(any()))
                .thenReturn(Optional.of(requester));
        when(itemRequestRepository.findAllByRequesterIdOrderByIdAsc(any()))
                .thenReturn(List.of(itemRequest));

        // Act
        var requests = itemRequestService.getItemRequestsByRequesterId(3L);

        // Assert
        assertEquals(1, requests.size());
        assertTrue(requests.stream().findFirst().isPresent());

        var firstRequest = requests.stream().findFirst().get();
        assertThat(firstRequest.getId(), notNullValue());
        assertEquals(itemRequest.getDescription(), firstRequest.getDescription());
        assertEquals(itemRequest.getItems().stream().findFirst().get().getId(),
                firstRequest.getItems().stream().findFirst().get().getId());
    }

    @Test
    public void getItemRequestsByRequesterId_withInvalidRequesterId_throwsNoSuchElementException()
            throws NoSuchElementException {
        // Act & Assert
        assertThrows(NoSuchElementException.class, () -> itemRequestService.getItemRequestsByRequesterId(3L));
    }

    @Test
    public void getItemRequests_withValidData_returnsItemRequestCollection() throws IllegalArgumentException {
        // Arrange
        var requester = new User(3L,
                "Requester Name",
                "requester.name@mail.com");
        var itemRequest = new ItemRequest(1L,
                "Description",
                requester,
                LocalDateTime.now(),
                Set.of(new Item(
                        1L,
                        "Item Name",
                        "Item Description",
                        true,
                        new User(1L, "Owner Name", "owner.name@mail.com"),
                        null)));

        when(userRepository.findById(any()))
                .thenReturn(Optional.of(requester));
        when(itemRequestRepository.findAllByRequesterIdNotOrderByIdAsc(any(), any()))
                .thenReturn(new PageImpl<>(List.of(itemRequest)));

        // Act
        var requests = itemRequestService.getItemRequests(3L, null, null);

        // Assert
        assertEquals(1, requests.size());
        assertTrue(requests.stream().findFirst().isPresent());

        var firstRequest = requests.stream().findFirst().get();
        assertThat(firstRequest.getId(), notNullValue());
        assertEquals(itemRequest.getDescription(), firstRequest.getDescription());
        assertEquals(itemRequest.getItems().stream().findFirst().get().getId(),
                firstRequest.getItems().stream().findFirst().get().getId());
    }

    @Test
    public void getItemRequests_withInvalidSearchParams_throwsIllegalArgumentException()
            throws IllegalArgumentException {
        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> itemRequestService.getItemRequests(3L, -1, -1));
    }

}
