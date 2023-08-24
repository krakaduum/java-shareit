package ru.practicum.shareit.item.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.item.model.Item;

import javax.validation.*;
import java.util.*;

@Component
@Qualifier("inMemoryItemStorage")
@Slf4j
public class InMemoryItemStorage implements ItemStorage {

    private final Validator validator;
    private final Map<Long, Item> items = new HashMap<>();
    private long currentId = 1;

    public InMemoryItemStorage() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        this.validator = factory.getValidator();
    }

    @Override
    public Item addItem(Item item) {
        Set<ConstraintViolation<Item>> violations = validator.validate(item);
        for (ConstraintViolation<Item> violation : violations) {
            log.error(violation.getMessage());
            throw new ValidationException("Валидация не пройдена");
        }

        if (items.containsKey(item.getId())) {
            throw new RuntimeException("Вещь с идентификатором " + item.getId() + " уже существует");
        }

        item.setId(currentId++);

        items.put(item.getId(), item);

        return item;
    }

    @Override
    public Item getItem(long id) {
        if (!items.containsKey(id)) {
            throw new NoSuchElementException("Вещи с идентификатором " + id + " не существует");
        }

        return items.get(id);
    }

    @Override
    public Item updateItem(Item item) {
        Set<ConstraintViolation<Item>> violations = validator.validate(item);
        for (ConstraintViolation<Item> violation : violations) {
            log.error(violation.getMessage());
            throw new ValidationException("Валидация не пройдена");
        }

        if (!items.containsKey(item.getId())) {
            throw new NoSuchElementException("Вещи с идентификатором " + item.getId() + " не существует");
        }

        items.put(item.getId(), item);

        return item;
    }

    @Override
    public void removeItem(long id) {
        if (!items.containsKey(id)) {
            throw new NoSuchElementException("Вещи с идентификатором " + id + " не существует");
        }

        items.remove(id);
    }

    @Override
    public Collection<Item> getItems() {
        return items.values();
    }

}
