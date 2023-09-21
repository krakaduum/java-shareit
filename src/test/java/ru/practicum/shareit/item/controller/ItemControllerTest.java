package ru.practicum.shareit.item.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.booking.dto.BookingShortDto;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.CommentRequestBody;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemExtendedDto;
import ru.practicum.shareit.item.service.ItemService;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = ItemController.class)
public class ItemControllerTest {

    private final ItemDto itemDto = new ItemDto(
            1L,
            "Item Name",
            "Item Description",
            true,
            1L,
            1L);

    private final ItemExtendedDto itemExtendedDto = new ItemExtendedDto(
            1L,
            "Item Name",
            "Item Description",
            true,
            1L,
            new BookingShortDto(2L, 2L),
            new BookingShortDto(3L, 3L),
            List.of(new CommentDto(1L, "Comment", "Author Name", LocalDateTime.now())));

    private final CommentRequestBody commentRequestBody = new CommentRequestBody("Text");

    private final CommentDto commentDto = new CommentDto(
            1L,
            "Text",
            "Author Name",
            LocalDateTime.now());

    @Autowired
    ObjectMapper mapper;
    @MockBean
    ItemService itemService;
    @Autowired
    MockMvc mvc;

    @Test
    public void addItemTest() throws Exception {
        // Arrange
        var headers = new HttpHeaders();
        headers.set("X-Sharer-User-Id", "1");

        when(itemService.addItem(anyLong(), any()))
                .thenReturn(itemDto);

        // Act & Assert
        mvc.perform(post("/items")
                        .headers(headers)
                        .content(mapper.writeValueAsString(itemDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(itemDto.getId()), Long.class))
                .andExpect(jsonPath("$.name", is(itemDto.getName())))
                .andExpect(jsonPath("$.description", is(itemDto.getDescription())))
                .andExpect(jsonPath("$.available", is(itemDto.getAvailable())))
                .andExpect(jsonPath("$.ownerId", is(itemDto.getOwnerId()), Long.class))
                .andExpect(jsonPath("$.requestId", is(itemDto.getRequestId()), Long.class));
    }

    @Test
    public void getItemTest() throws Exception {
        // Arrange
        var headers = new HttpHeaders();
        headers.set("X-Sharer-User-Id", "1");

        when(itemService.getItem(anyLong(), anyLong()))
                .thenReturn(itemExtendedDto);

        // Act & Assert
        mvc.perform(get("/items/1")
                        .headers(headers))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(itemExtendedDto.getId()), Long.class))
                .andExpect(jsonPath("$.name", is(itemExtendedDto.getName())))
                .andExpect(jsonPath("$.description", is(itemExtendedDto.getDescription())))
                .andExpect(jsonPath("$.available", is(itemExtendedDto.isAvailable())))
                .andExpect(jsonPath("$.ownerId", is(itemExtendedDto.getOwnerId()), Long.class))
                .andExpect(jsonPath("$.lastBooking.id", is(itemExtendedDto.getLastBooking().getId()), Long.class))
                .andExpect(jsonPath("$.lastBooking.bookerId", is(itemExtendedDto.getLastBooking().getBookerId()), Long.class))
                .andExpect(jsonPath("$.nextBooking.id", is(itemExtendedDto.getNextBooking().getId()), Long.class))
                .andExpect(jsonPath("$.nextBooking.bookerId", is(itemExtendedDto.getNextBooking().getBookerId()), Long.class));
    }

    @Test
    public void updateItemTest() throws Exception {
        // Arrange
        var headers = new HttpHeaders();
        headers.set("X-Sharer-User-Id", "1");

        when(itemService.updateItem(anyLong(), anyLong(), any()))
                .thenReturn(itemDto);

        // Act & Assert
        mvc.perform(patch("/items/1")
                        .headers(headers)
                        .content(mapper.writeValueAsString(itemDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(itemDto.getId()), Long.class))
                .andExpect(jsonPath("$.name", is(itemDto.getName())))
                .andExpect(jsonPath("$.description", is(itemDto.getDescription())))
                .andExpect(jsonPath("$.available", is(itemDto.getAvailable())))
                .andExpect(jsonPath("$.ownerId", is(itemDto.getOwnerId()), Long.class))
                .andExpect(jsonPath("$.requestId", is(itemDto.getRequestId()), Long.class));
    }

    @Test
    public void removeItemTest() throws Exception {
        // Arrange
        var headers = new HttpHeaders();
        headers.set("X-Sharer-User-Id", "1");

        // Act & Assert
        mvc.perform(delete("/items/1")
                        .headers(headers)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void getItemsByOwnerIdTest() throws Exception {
        // Arrange
        var headers = new HttpHeaders();
        headers.set("X-Sharer-User-Id", "1");

        when(itemService.getItemsByOwnerId(anyLong(), any(), any()))
                .thenReturn(List.of(itemExtendedDto));

        // Act & Assert
        mvc.perform(get("/items")
                        .headers(headers))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id", is(itemExtendedDto.getId()), Long.class))
                .andExpect(jsonPath("$[0].name", is(itemExtendedDto.getName())))
                .andExpect(jsonPath("$[0].description", is(itemExtendedDto.getDescription())))
                .andExpect(jsonPath("$[0].available", is(itemExtendedDto.isAvailable())))
                .andExpect(jsonPath("$[0].ownerId", is(itemExtendedDto.getOwnerId()), Long.class))
                .andExpect(jsonPath("$[0].lastBooking.id", is(itemExtendedDto.getLastBooking().getId()), Long.class))
                .andExpect(jsonPath("$[0].lastBooking.bookerId", is(itemExtendedDto.getLastBooking().getBookerId()), Long.class))
                .andExpect(jsonPath("$[0].nextBooking.id", is(itemExtendedDto.getNextBooking().getId()), Long.class))
                .andExpect(jsonPath("$[0].nextBooking.bookerId", is(itemExtendedDto.getNextBooking().getBookerId()), Long.class));
    }

    @Test
    public void searchItemsTest() throws Exception {
        // Arrange
        when(itemService.searchItems(anyString(), any(), any()))
                .thenReturn(List.of(itemDto));

        // Act & Assert
        mvc.perform(get("/items/search")
                        .param("text", "TEXT"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id", is(itemDto.getId()), Long.class))
                .andExpect(jsonPath("$[0].name", is(itemDto.getName())))
                .andExpect(jsonPath("$[0].description", is(itemDto.getDescription())))
                .andExpect(jsonPath("$[0].available", is(itemDto.getAvailable())))
                .andExpect(jsonPath("$[0].ownerId", is(itemDto.getOwnerId()), Long.class))
                .andExpect(jsonPath("$[0].requestId", is(itemDto.getRequestId()), Long.class));
    }

    @Test
    public void addCommentTest() throws Exception {
        // Arrange
        var headers = new HttpHeaders();
        headers.set("X-Sharer-User-Id", "1");

        when(itemService.addComment(anyLong(), anyLong(), any()))
                .thenReturn(commentDto);

        // Act & Assert
        mvc.perform(post("/items/1/comment")
                        .headers(headers)
                        .content(mapper.writeValueAsString(commentRequestBody))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(commentDto.getId()), Long.class))
                .andExpect(jsonPath("$.text", is(commentDto.getText())))
                .andExpect(jsonPath("$.authorName", is(commentDto.getAuthorName())));
    }

}
