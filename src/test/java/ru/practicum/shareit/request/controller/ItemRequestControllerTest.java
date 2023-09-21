package ru.practicum.shareit.request.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestRequestBody;
import ru.practicum.shareit.request.service.ItemRequestService;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = ItemRequestController.class)
public class ItemRequestControllerTest {

    private final ItemRequestRequestBody itemRequestRequestBody = new ItemRequestRequestBody("Description");

    private final ItemRequestDto itemRequestDto = new ItemRequestDto(1L,
            "Description",
            LocalDateTime.now(),
            Set.of(new ItemDto(
                    1L,
                    "Item Name",
                    "Item Description",
                    true,
                    1L,
                    1L)));

    @Autowired
    ObjectMapper mapper;
    @MockBean
    ItemRequestService itemRequestService;
    @Autowired
    MockMvc mvc;

    @Test
    public void addItemRequest_withValidData_returnsOk() throws Exception {
        // Arrange
        var headers = new HttpHeaders();
        headers.set("X-Sharer-User-Id", "1");

        when(itemRequestService.addItemRequest(anyLong(), any()))
                .thenReturn(itemRequestDto);

        // Act & Assert
        mvc.perform(post("/requests")
                        .headers(headers)
                        .content(mapper.writeValueAsString(itemRequestRequestBody))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(itemRequestDto.getId()), Long.class))
                .andExpect(jsonPath("$.description", is(itemRequestDto.getDescription())))
                .andExpect(jsonPath("$.items[0].id", is(itemRequestDto.getItems().stream().findFirst().get().getId()), Long.class));
    }

    @Test
    public void getItemRequest_withValidData_returnsOk() throws Exception {
        // Arrange
        var headers = new HttpHeaders();
        headers.set("X-Sharer-User-Id", "1");

        when(itemRequestService.getItemRequest(anyLong(), anyLong()))
                .thenReturn(itemRequestDto);

        // Act & Assert
        mvc.perform(get("/requests/1")
                        .headers(headers))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(itemRequestDto.getId()), Long.class))
                .andExpect(jsonPath("$.description", is(itemRequestDto.getDescription())))
                .andExpect(jsonPath("$.items[0].id", is(itemRequestDto.getItems().stream().findFirst().get().getId()), Long.class));
    }

    @Test
    public void getItemRequestsByRequesterId_withValidData_returnsOk() throws Exception {
        // Arrange
        var headers = new HttpHeaders();
        headers.set("X-Sharer-User-Id", "1");

        when(itemRequestService.getItemRequestsByRequesterId(anyLong()))
                .thenReturn(List.of(itemRequestDto));

        // Act & Assert
        mvc.perform(get("/requests")
                        .headers(headers))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id", is(itemRequestDto.getId()), Long.class))
                .andExpect(jsonPath("$[0].description", is(itemRequestDto.getDescription())))
                .andExpect(jsonPath("$[0].items[0].id", is(itemRequestDto.getItems().stream().findFirst().get().getId()), Long.class));
    }

    @Test
    public void getItemRequests_withValidData_returnsOk() throws Exception {
        // Arrange
        var headers = new HttpHeaders();
        headers.set("X-Sharer-User-Id", "1");

        when(itemRequestService.getItemRequests(anyLong(), any(), any()))
                .thenReturn(List.of(itemRequestDto));

        // Act & Assert
        mvc.perform(get("/requests/all")
                        .headers(headers))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id", is(itemRequestDto.getId()), Long.class))
                .andExpect(jsonPath("$[0].description", is(itemRequestDto.getDescription())))
                .andExpect(jsonPath("$[0].items[0].id", is(itemRequestDto.getItems().stream().findFirst().get().getId()), Long.class));
    }

}
