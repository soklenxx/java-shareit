package ru.practicum.shareit.user;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface UserService {
    List<User> findAllUsers();

    User getUserById(Long id);

    User createUser(User user);

    User updateUser(User user, Long id);

    void deleteUser(Long id);
}
