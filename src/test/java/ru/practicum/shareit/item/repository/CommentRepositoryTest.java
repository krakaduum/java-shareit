package ru.practicum.shareit.item.repository;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.repository.ItemRequestRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.Set;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.assertEquals;

@RequiredArgsConstructor(onConstructor_ = @Autowired)
@DataJpaTest
public class CommentRepositoryTest {

    private final CommentRepository commentRepository;
    private final ItemRepository itemRepository;
    private final ItemRequestRepository itemRequestRepository;
    private final UserRepository userRepository;

    @Test
    public void findAllByItemIdOrderByIdAscTest() {
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
        var itemRequest = new ItemRequest(1L, "Description", author, LocalDateTime.now(), Set.of(item));
        var comment = new Comment(1L, "Text", item, author, LocalDateTime.now());

        userRepository.save(owner);
        userRepository.save(author);
        item = itemRepository.save(item);
        itemRequest = itemRequestRepository.save(itemRequest);
        item.setRequest(itemRequest);
        itemRepository.save(item);
        commentRepository.save(comment);

        // Act
        var comments = commentRepository.findAllByItemIdOrderByIdAsc(item.getId());
        var firstComment = comments.stream().findFirst().get();

        // Assert
        assertEquals(1, comments.size());
        assertThat(firstComment.getId(), notNullValue());
        assertEquals(comment.getText(), firstComment.getText());
        assertEquals(comment.getItem().getId(), firstComment.getItem().getId());
        assertEquals(comment.getAuthor().getId(), firstComment.getAuthor().getId());
        assertEquals(comment.getCreated(), firstComment.getCreated());
    }

}
