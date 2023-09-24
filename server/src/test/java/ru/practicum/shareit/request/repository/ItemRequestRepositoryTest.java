package ru.practicum.shareit.request.repository;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.jdbc.Sql;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@RequiredArgsConstructor(onConstructor_ = @Autowired)
@AutoConfigureTestDatabase
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@Sql(scripts = "classpath:schema.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@DataJpaTest
public class ItemRequestRepositoryTest {

    private final ItemRequestRepository itemRequestRepository;
    private final UserRepository userRepository;

    @Test
    public void findAllByRequesterIdOrderByIdAsc_withValidSearchParams_returnsItemRequestCollection() {
        // Arrange
        var requester = new User(1L,
                "Requester Name",
                "requester.name@mail.com");
        var itemRequest = new ItemRequest(1L, "Description", requester, LocalDateTime.now(), null);

        userRepository.save(requester);
        itemRequestRepository.save(itemRequest);

        // Act
        var requests = itemRequestRepository.findAllByRequesterIdOrderByIdAsc(requester.getId());


        // Assert
        assertEquals(1, requests.size());
        assertTrue(requests.stream().findFirst().isPresent());

        var firstRequest = requests.stream().findFirst().get();
        assertThat(firstRequest.getId(), notNullValue());
        assertEquals(itemRequest.getDescription(), firstRequest.getDescription());
        assertEquals(itemRequest.getRequester().getId(), firstRequest.getRequester().getId());
    }

    @Test
    public void findAllByRequesterIdNotOrderByIdAscAsc_withValidSearchParams_returnsItemRequestCollection() {
        // Arrange
        var requester1 = new User(1L,
                "Requester1 Name",
                "requester1.name@mail.com");
        var requester2 = new User(2L,
                "Requester2 Name",
                "requester2.name@mail.com");
        var itemRequest1 = new ItemRequest(1L, "Description1", requester1, LocalDateTime.now(), null);
        var itemRequest2 = new ItemRequest(2L, "Description2", requester2, LocalDateTime.now(), null);

        userRepository.save(requester1);
        userRepository.save(requester2);
        itemRequestRepository.save(itemRequest1);
        itemRequestRepository.save(itemRequest2);

        // Act
        var requests = itemRequestRepository.findAllByRequesterIdNotOrderByIdAsc(requester1.getId(), Pageable.unpaged())
                .getContent();

        // Assert
        assertEquals(1, requests.size());
        assertTrue(requests.stream().findFirst().isPresent());

        var firstRequest = requests.stream().findFirst().get();
        assertThat(firstRequest.getId(), notNullValue());
        assertEquals(itemRequest2.getDescription(), firstRequest.getDescription());
        assertEquals(itemRequest2.getRequester().getId(), firstRequest.getRequester().getId());
    }

}
