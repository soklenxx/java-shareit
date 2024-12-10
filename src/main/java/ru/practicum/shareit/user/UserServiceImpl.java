package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.exceptions.ValidationException;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Override
    public List<User> findAllUsers() {
        log.info("Request to get Users");
        return userRepository.findAll();
    }

    @Override
    public User createUser(User user) {
        log.info("Request to create User by email {}", user.getEmail());
        // проверяем выполнение необходимых условий
        validate(user);
        // формируем дополнительные данные
        return userRepository.save(user);
    }

    @Override
    public User updateUser(@RequestBody User user, Long id) {
        validate(user);
        return userRepository.save(user);
    }

    @Override
    public void deleteUser(Long id) {
        getUserById(id);
        userRepository.deleteById(id);
        log.info("Request to delete User by id {}", id);
    }

    @Override
    public User getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Пользователь с id = " + id + " не найден"));
    }

    private void validate(User user) {
        if (user.getEmail() == null || user.getEmail().isBlank()) {
            log.error("Empty User email");
            throw new ValidationException("Имейл должен быть указан");
        }
        if (!user.getEmail().contains("@")) {
            throw new ValidationException("Имейл должен содержать символ @");
        }
        Optional<User> userBd = userRepository.findByEmail(user.getEmail());
        if (userBd.isPresent() && !userBd.get().getId().equals(user.getId())) {
            throw new RuntimeException("Имейл \"%s\" уже есть в системе".formatted(user.getEmail()));
        }
    }
}
