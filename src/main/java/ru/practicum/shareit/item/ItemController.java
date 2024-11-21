package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.shareit.item.dto.CreateItemDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.UpdateItemDto;

import java.util.List;

/**
 * TODO Sprint add-controllers.
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/items")
public class ItemController {

    private final ItemService itemService;
    private final ItemMapper itemMapper;

    @GetMapping
    public ResponseEntity<List<ItemDto>> getItems(@RequestHeader("X-Sharer-User-Id") Long userId) {
        return ResponseEntity.ok(itemMapper.toItemDto(itemService.getItems(userId)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ItemDto> getItemById(@PathVariable("id") Long id) {
        return ResponseEntity.ok(itemMapper.toItemDto(itemService.getItemById(id)));
    }

    @PostMapping
    public ResponseEntity<ItemDto> createItem(@RequestBody CreateItemDto createItemDto,
                                              @RequestHeader("X-Sharer-User-Id") Long userId) {
        Item item = itemMapper.toEntity(createItemDto);
        return ResponseEntity.ok(itemMapper.toItemDto(itemService.createItem(item, userId)));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ItemDto> updateItem(@PathVariable("id") Long id,
                                              @RequestHeader("X-Sharer-User-Id") Long userId,
                                              @RequestBody UpdateItemDto updateItemDto) {
        Item item = itemMapper.updateEntity(updateItemDto, id);
        return ResponseEntity.ok(itemMapper.toItemDto(itemService.updateItem(id, userId, item)));
    }

    @GetMapping("/search")
    public ResponseEntity<List<ItemDto>> getItemsByText(@RequestParam(required = false) String text) {
        return ResponseEntity.ok(itemMapper.toItemDto(itemService.getItemsByText(text)));
    }
}
