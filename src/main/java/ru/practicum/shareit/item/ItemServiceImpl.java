package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.booking.BookingStatus;
import ru.practicum.shareit.comments.Comment;
import ru.practicum.shareit.comments.CommentsRepository;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.exceptions.ValidationException;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserService;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {
    private final UserService userService;
    private final ItemRepository itemRepository;
    private final CommentsRepository commentsRepository;
    private final BookingRepository bookingRepository;

    @Override
    public List<Item> getItems(Long userId) {
        log.info("Request to get Items");
        return itemRepository.findByOwnerId(userId);
    }

    @Override
    public Item createItem(@RequestBody Item item, Long userId) {
        log.info("Request to create Item");
        validate(item);
        User user = userService.getUserById(userId);
        item.setOwner(user);
        return itemRepository.save(item);
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
        validate(item);
        if (itemBD.getOwner().getId().equals(userId)) {
            return itemRepository.save(item);
        }
        throw new NotFoundException("Товар с id = " + item.getId() + " не найден");
    }

    @Override
    public Item getItemById(Long id) {
        return itemRepository.findById(id).orElseThrow(() ->  new NotFoundException("Пользователь с id = " + id + " не найден"));
    }

    @Override
    public List<Item> getItemsByText(String text) {
        log.info("Request to get Items by text {}", text);
        return (!text.isEmpty())
                ? itemRepository.findByAvailableTrueAndNameIgnoreCaseContainingOrDescriptionIgnoreCaseContaining(text, text)
                : List.of();
    }

    @Override
    public Comment createComment(Comment comment, Long id, Long userId) {
        Item item = getItemById(id);
        User user = userService.getUserById(userId);
        if (bookingRepository.findBookingByBookerAndItemAndStatus(user,item, BookingStatus.APPROVED, LocalDateTime.now()).isEmpty()) {
            throw new ValidationException("Данный пользователь не брал вещь в аренду");
        }
        LocalDateTime created = LocalDateTime.now();
        comment.setCreated(created);
        comment.setItem(item);
        comment.setAuthor(user);
        return commentsRepository.save(comment);
    }

    @Override
    public List<Comment> getComments(Long id) {
        Item item = getItemById(id);
        return commentsRepository.findByItem(item);
    }

    @Override
    public LocalDateTime findLastBookingByItem(Item item) {
        return bookingRepository.findLastBookingByItem(item, LocalDateTime.now(), BookingStatus.WAITING).orElse(null);
    }

    @Override
    public LocalDateTime findNextBookingByItem(Item item) {
        return bookingRepository.findNextBookingByItem(item, LocalDateTime.now(), BookingStatus.APPROVED).orElse(null);
    }

    private void validate(Item item) {
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
}
