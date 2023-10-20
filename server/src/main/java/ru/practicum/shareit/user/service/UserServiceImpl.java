package ru.practicum.shareit.user.service;

import org.springframework.stereotype.Service;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import javax.validation.*;
import java.util.Collection;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final Validator validator;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;

        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        this.validator = factory.getValidator();
    }

    @Override
    public UserDto addUser(UserDto userDto) {
        User user = UserMapper.toUser(userDto);
        user.setId(null);

        Set<ConstraintViolation<User>> violations = validator.validate(user);
        for (ConstraintViolation<User> violation : violations) {
            throw new ValidationException("Валидация не пройдена: " + violation.getMessage());
        }

        user = userRepository.save(user);
        return UserMapper.toUserDto(user);

    }

    @Override
    public UserDto getUser(long id) {
        Optional<User> user = userRepository.findById(id);

        if (user.isEmpty()) {
            throw new NoSuchElementException("Пользователя с идентификатором " + id + " не существует");
        }

        return UserMapper.toUserDto(user.get());
    }

    @Override
    public UserDto updateUser(long id, UserDto userDto) {
        Optional<User> existingUser = userRepository.findById(id);

        if (existingUser.isEmpty()) {
            throw new NoSuchElementException("Пользователя с идентификатором " + id + " не существует");
        }

        if (userDto.getName() != null && !userDto.getName().trim().isEmpty()) {
            existingUser.get().setName(userDto.getName());
        }

        if (userDto.getEmail() != null && !userDto.getEmail().trim().isEmpty()) {
            existingUser.get().setEmail(userDto.getEmail());
        }

        Set<ConstraintViolation<User>> violations = validator.validate(existingUser.get());
        for (ConstraintViolation<User> violation : violations) {
            throw new ValidationException("Валидация не пройдена: " + violation.getMessage());
        }

        User updatedUser = userRepository.save(existingUser.get());
        return UserMapper.toUserDto(updatedUser);
    }

    @Override
    public void removeUser(long id) {
        if (userRepository.existsById(id)) {
            userRepository.deleteById(id);
        } else {
            throw new NoSuchElementException("Пользователя с идентификатором " + id + " не существует");
        }
    }

    @Override
    public Collection<UserDto> getUsers() {
        return userRepository
                .findAll()
                .stream()
                .map(UserMapper::toUserDto)
                .collect(Collectors.toList());
    }

}
