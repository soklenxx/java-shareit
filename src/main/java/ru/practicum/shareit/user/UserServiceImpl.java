package ru.practicum.shareit.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.exceptions.ValidationException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
public class UserServiceImpl implements UserService {
    private final Map<Long, User> users = new HashMap<>();

    @Override
    public List<User> findAllUsers() {
        log.info("Request to get Users");
        return users.values().stream().toList();
    }

    @Override
    public User createUser(User user) {
        log.info("Request to create User by email {}", user.getEmail());
        // проверяем выполнение необходимых условий
        validation(user);
        // формируем дополнительные данные
        user.setId(getNextId());
        // сохраняем новую публикацию в памяти приложения
        users.put(user.getId(), user);
        return user;
    }

    @Override
    public User updateUser(@RequestBody User user, Long id) {
        // проверяем необходимые условия
        if (id == null) {
            log.error("Empty User id");
            throw new ValidationException("Id должен быть указан");
        }
        log.info("Request to update User by id {}", id);
        validation(user);
        if (users.containsKey(id)) {
            User oldUser = users.get(id);
            oldUser.setName(user.getName());
            oldUser.setEmail(user.getEmail());
            return oldUser;
        }
        throw new NotFoundException("Пользователь с id = " + id + " не найден");
    }

    @Override
    public void deleteUser(Long id) {
        getUserById(id);
        users.remove(id);
        log.info("Request to delete User by id {}", id);
    }

    @Override
    public User getUserById(Long id) {
        if (users.containsKey(id)) {
            return users.get(id);
        }
        throw new NotFoundException("Пользователь с id = " + id + " не найден");
    }

    private void validation(User user) {
        if (user.getEmail() == null || user.getEmail().isBlank()) {
            log.error("Empty User email");
            throw new ValidationException("Имейл должен быть указан");
        }
        if (!user.getEmail().contains("@")) {
            throw new ValidationException("Имейл должен содержать символ @");
        }
        if (users.values().stream().filter(u -> !u.getId().equals(user.getId())).map(User::getEmail).anyMatch(n -> n.equals(user.getEmail()))) {
            throw new RuntimeException("Имейл \"%s\" уже есть в системе".formatted(user.getEmail()));
        }
    }

    private Long getNextId() {
        Long currentMaxId = users.keySet()
                .stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }
}
