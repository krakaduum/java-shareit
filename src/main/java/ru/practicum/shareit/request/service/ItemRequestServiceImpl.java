package ru.practicum.shareit.request.service;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestRequestBody;
import ru.practicum.shareit.request.mapper.ItemRequestMapper;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.repository.ItemRequestRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import javax.validation.*;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional
public class ItemRequestServiceImpl implements ItemRequestService {

    private final ItemRequestRepository itemRequestRepository;
    private final UserRepository userRepository;
    private final Validator validator;

    public ItemRequestServiceImpl(ItemRequestRepository itemRequestRepository, UserRepository userRepository) {
        this.itemRequestRepository = itemRequestRepository;
        this.userRepository = userRepository;

        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        this.validator = factory.getValidator();
    }

    @Override
    public ItemRequestDto addItemRequest(long requesterId, ItemRequestRequestBody itemRequestRequestBody) {
        ItemRequest itemRequest = new ItemRequest();
        itemRequest.setId(null);
        itemRequest.setCreated(LocalDateTime.now());

        Optional<User> requester = userRepository.findById(requesterId);

        if (requester.isEmpty()) {
            throw new NoSuchElementException("Пользователя с идентификатором " + requesterId + " не существует");
        }

        itemRequest.setDescription(itemRequestRequestBody.getDescription());
        itemRequest.setRequester(requester.get());

        Set<ConstraintViolation<ItemRequest>> violations = validator.validate(itemRequest);
        for (ConstraintViolation<ItemRequest> violation : violations) {
            throw new ValidationException("Валидация не пройдена: " + violation.getMessage());
        }

        itemRequest = itemRequestRepository.save(itemRequest);
        return ItemRequestMapper.toItemRequestDto(itemRequest);
    }

    @Override
    public ItemRequestDto getItemRequest(long userId, long requestId) {
        Optional<User> user = userRepository.findById(userId);

        if (user.isEmpty()) {
            throw new NoSuchElementException("Пользователя с идентификатором " + userId + " не существует");
        }


        Optional<ItemRequest> itemRequest = itemRequestRepository.findById(requestId);

        if (itemRequest.isEmpty()) {
            throw new NoSuchElementException("Запроса с идентификатором " + requestId + " не существует");
        }

        return ItemRequestMapper.toItemRequestDto(itemRequest.get());
    }

    @Override
    public Collection<ItemRequestDto> getItemRequestsByRequesterId(long requesterId) {
        Optional<User> requester = userRepository.findById(requesterId);

        if (requester.isEmpty()) {
            throw new NoSuchElementException("Пользователя с идентификатором " + requesterId + " не существует");
        }

        return itemRequestRepository
                .findAllByRequesterIdOrderByIdAsc(requesterId)
                .stream()
                .map(ItemRequestMapper::toItemRequestDto)
                .collect(Collectors.toList());
    }

    @Override
    public Collection<ItemRequestDto> getItemRequests(long userId, Integer from, Integer size) {
        if ((from != null && from < 0) || (size != null && size <= 0)) {
            throw new IllegalArgumentException("Неверные параметры поиска");
        }

        if (from == null) {
            from = 0;
        }

        if (size == null) {
            size = Integer.MAX_VALUE;
        }

        return itemRequestRepository
                .findAllByRequesterIdNotOrderByIdAsc(userId, Pageable.ofSize(from + size))
                .stream()
                .skip(from)
                .map(ItemRequestMapper::toItemRequestDto)
                .collect(Collectors.toList());
    }

}
