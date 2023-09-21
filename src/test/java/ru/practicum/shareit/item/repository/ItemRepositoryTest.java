package ru.practicum.shareit.item.repository;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.jdbc.Sql;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.assertEquals;

@RequiredArgsConstructor(onConstructor_ = @Autowired)
@AutoConfigureTestDatabase
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@Sql(scripts = "classpath:schema.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@DataJpaTest
public class ItemRepositoryTest {

    private final ItemRepository itemRepository;
    private final UserRepository userRepository;

    @Test
    public void findAllByOwnerIdOrderByIdAscTest() {
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

        userRepository.save(owner);
        itemRepository.save(item);

        // Act
        var items = itemRepository.findAllByOwnerIdOrderByIdAsc(owner.getId(), Pageable.unpaged()).getContent();
        var firstItem = items.stream().findFirst().get();

        // Assert
        assertEquals(1, items.size());
        assertThat(firstItem.getId(), notNullValue());
        assertEquals(item.getName(), firstItem.getName());
        assertEquals(item.getDescription(), firstItem.getDescription());
        assertEquals(item.getAvailable(), firstItem.getAvailable());
        assertEquals(item.getOwner().getId(), firstItem.getOwner().getId());
    }

    @Test
    public void findAllAvailableItemsByNameOrDescriptionTest() {
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

        userRepository.save(owner);
        itemRepository.save(item);

        // Act
        var items = itemRepository.findAllAvailableItemsByNameOrDescription("Item Name", Pageable.unpaged()).getContent();
        var firstItem = items.stream().findFirst().get();

        // Assert
        assertEquals(1, items.size());
        assertThat(firstItem.getId(), notNullValue());
        assertEquals(item.getName(), firstItem.getName());
        assertEquals(item.getDescription(), firstItem.getDescription());
        assertEquals(item.getAvailable(), firstItem.getAvailable());
        assertEquals(item.getOwner().getId(), firstItem.getOwner().getId());
    }

}
