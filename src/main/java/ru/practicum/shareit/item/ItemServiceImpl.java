package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.exceptions.ValidationException;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {
    private final Map<Long, Item> items = new HashMap<>();
    private final UserService userService;

    @Override
    public List<Item> getItems(Long userId) {
        log.info("Request to get Items");
        return items.values().stream().filter(i -> i.getOwner().getId() == userId).toList();
    }

    @Override
    public Item createItem(@RequestBody Item item, Long userId) {
        log.info("Request to create Item");
        validation(item);
        item.setId(getNextId());
        User user = userService.getUserById(userId);
        item.setOwner(user);
        items.put(item.getId(), item);
        return item;
    }

    @Override
    public Item updateItem(Long id, Long userId, @RequestBody Item item) {
        // проверяем необходимые условия
        if (id == null) {
            log.error("Empty Item id");
            throw new ValidationException("Id должен быть указан");
        }
        log.info("Request to update User by id {}", item.getId());
        Item itemBD = getItemById(id);
        validation(item);
        if (items.containsKey(id) & itemBD.getOwner().getId() == userId) {
            Item oldUser = items.get(id);
            oldUser.setName(item.getName());
            oldUser.setDescription(item.getDescription());
            oldUser.setAvailable(item.getAvailable());
            return oldUser;
        }
        throw new NotFoundException("Товар с id = " + item.getId() + " не найден");
    }

    @Override
    public Item getItemById(Long id) {
        if (items.containsKey(id)) {
            return items.get(id);
        }
        throw new NotFoundException("Пользователь с id = " + id + " не найден");
    }

    @Override
    public List<Item> getItemsByText(String text) {
        log.info("Request to get Items by text {}", text);
        return items.values().stream().filter(i ->
                !text.isEmpty() && i.getAvailable() &&
                        (i.getName().toUpperCase().contains(text.toUpperCase()) ||
                                i.getDescription().toUpperCase().contains(text.toUpperCase()))).toList();
    }

    private void validation(Item item) {
        if (item.getName() == null || item.getName().isBlank()) {
            log.error("Empty Item name");
            throw new ValidationException("Название товара должно быть указано");
        }
        if (item.getDescription() == null || item.getDescription().isBlank()) {
            log.error("Empty Item descriptoion");
            throw new ValidationException("Описание товара должно быть указано");
        }
        if (item.getAvailable() == null) {
            throw new ValidationException("Статус товара должен быть указан");
        }
    }


    // вспомогательный метод для генерации идентификатора нового поста
    private Long getNextId() {
        Long currentMaxId = items.keySet()
                .stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }
}
