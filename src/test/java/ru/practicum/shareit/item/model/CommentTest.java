package ru.practicum.shareit.item.model;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class CommentTest {

    @Test
    public void constructor_withValidData_returnsComment() {
        // Arrange
        long id = 1L;
        String text = "Text";
        var item = new Item(1L,
                "Item Name",
                "Item Description",
                true,
                new User(1L, "Owner Name", "owner.name@mail.com"),
                null);
        var author = new User(2L, "Author Name", "author.name@mail.com");
        LocalDateTime created = LocalDateTime.now();

        // Act
        var comment = new Comment(1L, text, item, author, created);

        // Assert
        assertEquals(id, comment.getId());
        assertEquals(text, comment.getText());
        assertEquals(item.getId(), comment.getItem().getId());
        assertEquals(author.getId(), comment.getAuthor().getId());
    }

}
