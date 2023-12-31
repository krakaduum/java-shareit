package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.Constants;
import ru.practicum.shareit.request.dto.ItemRequestDto;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

@Controller
@RequestMapping(path = "/requests")
@RequiredArgsConstructor
@Slf4j
@Validated
public class ItemRequestController {

    private final ItemRequestClient itemRequestClient;

    @PostMapping
    public ResponseEntity<Object> addItemRequest(@RequestHeader(Constants.UserIdRequestHeaderName) long userId,
                                                 @Valid @RequestBody ItemRequestDto itemRequestDto) {
        log.info("Create item request {}, userId={}", itemRequestDto, userId);
        return itemRequestClient.addItemRequest(userId, itemRequestDto);
    }

    @GetMapping("/{requestId}")
    public ResponseEntity<Object> getItemRequest(@RequestHeader(Constants.UserIdRequestHeaderName) long userId,
                                                 @PathVariable Long requestId) {
        log.info("Get item request {}, userId={}", requestId, userId);
        return itemRequestClient.getItemRequest(userId, requestId);
    }

    @GetMapping
    public ResponseEntity<Object> getItemRequestsByRequesterId(@RequestHeader(Constants.UserIdRequestHeaderName) long userId) {
        log.info("Get item requests by requester, userId={}", userId);
        return itemRequestClient.getItemRequestsByRequesterId(userId);
    }

    @GetMapping("/all")
    public ResponseEntity<Object> getItemRequests(@RequestHeader(Constants.UserIdRequestHeaderName) long userId,
                                                  @PositiveOrZero @RequestParam(name = "from", defaultValue = "0") Integer from,
                                                  @Positive @RequestParam(name = "size", defaultValue = "10") Integer size) {
        log.info("Get item requests, from={}, size={}, userId={}", from, size, userId);
        return itemRequestClient.getItemRequests(userId, from, size);
    }

}
