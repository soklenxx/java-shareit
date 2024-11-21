package ru.practicum.shareit.item;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ItemService {
    List<Item> getItems(Long userId);
    Item getItemById(Long id);
    Item createItem(Item item, Long userId);
    Item updateItem(Long id, Long userId, Item item);
    List<Item> getItemsByText(String text);
}
