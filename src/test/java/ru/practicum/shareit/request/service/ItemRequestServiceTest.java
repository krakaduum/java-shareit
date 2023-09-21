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

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.openMocks;

@SpringBootTest
public class ItemRequestServiceTest {

    private final ItemRequestRequestBody itemRequestRequestBody = new ItemRequestRequestBody("Description");

    private final ItemRequest itemRequest = new ItemRequest(1L,
            "Description",
            new User(3L, "Requester Name", "requester.name@mail.com"),
            LocalDateTime.now(),
            Set.of(new Item(
                    1L,
                    "Item Name",
                    "Item Description",
                    true,
                    new User(1L, "Owner Name", "owner.name@mail.com"),
                    null)));

    private final User requester = new User(3L,
            "Requester Name",
            "requester.name@mail.com");

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
    public void addItemRequestTest() {
        // Arrange
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
    public void getItemRequestTest() {
        // Arrange
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
    public void getItemRequestsByRequesterIdTest() {
        // Arrange
        when(userRepository.findById(any()))
                .thenReturn(Optional.of(requester));
        when(itemRequestRepository.findAllByRequesterIdOrderByIdAsc(any()))
                .thenReturn(List.of(itemRequest));

        // Act
        var requests = itemRequestService.getItemRequestsByRequesterId(3L);
        var firstRequest = requests.stream().findFirst().get();

        // Assert
        assertEquals(1, requests.size());
        assertThat(firstRequest.getId(), notNullValue());
        assertEquals(itemRequest.getDescription(), firstRequest.getDescription());
        assertEquals(itemRequest.getItems().stream().findFirst().get().getId(),
                firstRequest.getItems().stream().findFirst().get().getId());
    }

    @Test
    public void getItemRequestsTest() {
        // Arrange
        when(userRepository.findById(any()))
                .thenReturn(Optional.of(requester));
        when(itemRequestRepository.findAllByRequesterIdNotOrderByIdAsc(any(), any()))
                .thenReturn(new PageImpl(List.of(itemRequest)));

        // Act
        var requests = itemRequestService.getItemRequests(3L, null, null);
        var firstRequest = requests.stream().findFirst().get();

        // Assert
        assertEquals(1, requests.size());
        assertThat(firstRequest.getId(), notNullValue());
        assertEquals(itemRequest.getDescription(), firstRequest.getDescription());
        assertEquals(itemRequest.getItems().stream().findFirst().get().getId(),
                firstRequest.getItems().stream().findFirst().get().getId());
    }

}
