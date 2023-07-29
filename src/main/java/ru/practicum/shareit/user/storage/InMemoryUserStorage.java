package ru.practicum.shareit.user.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.user.model.User;

import javax.validation.*;
import java.util.*;

@Component
@Qualifier("inMemoryUserStorage")
@Slf4j
public class InMemoryUserStorage implements UserStorage {

    private final Validator validator;
    private final Map<Long, User> users = new HashMap<>();
    private long currentId = 1;

    public InMemoryUserStorage() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        this.validator = factory.getValidator();
    }

    @Override
    public User addUser(User user) {
        Set<ConstraintViolation<User>> violations = validator.validate(user);
        for (ConstraintViolation<User> violation : violations) {
            log.error(violation.getMessage());
            throw new ValidationException("Валидация не пройдена");
        }

        if (users.values().stream().anyMatch(x -> x.getEmail().equals(user.getEmail()))) {
            throw new RuntimeException("Пользователь с электронной почтой " + user.getEmail() + " уже существует");
        }

        user.setId(currentId++);

        users.put(user.getId(), user);

        return user;
    }

    @Override
    public User getUser(long id) {
        if (!users.containsKey(id)) {
            throw new NoSuchElementException("Пользователя с идентификатором " + id + " не существует");
        }

        return users.get(id);
    }

    @Override
    public User updateUser(User user) {
        Set<ConstraintViolation<User>> violations = validator.validate(user);
        for (ConstraintViolation<User> violation : violations) {
            log.error(violation.getMessage());
            throw new ValidationException("Валидация не пройдена");
        }

        if (!users.containsKey(user.getId())) {
            throw new NoSuchElementException("Пользователя с идентификатором " + user.getId() + " не существует");
        }

        if (users.values().stream().anyMatch(x -> x.getEmail().equals(user.getEmail())
                && x.getId() != user.getId())) {
            throw new RuntimeException("Пользователь с электронной почтой " + user.getEmail() + " уже существует");
        }

        users.put(user.getId(), user);

        return user;
    }

    @Override
    public void removeUser(long id) {
        if (!users.containsKey(id)) {
            throw new NoSuchElementException("Пользователя с идентификатором " + id + " не существует");
        }

        users.remove(id);
    }

    @Override
    public Collection<User> getUsers() {
        return users.values();
    }

}
