package ru.practicum.shareit.item;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.shareit.comments.dto.CreateCommentDto;
import ru.practicum.shareit.item.dto.CreateItemDto;
import ru.practicum.shareit.item.dto.UpdateItemDto;

import static ru.practicum.shareit.Constants.USER_ID;

@RestController
@RequiredArgsConstructor
@Validated
@RequestMapping("/items")
public class ItemController {

    private final ItemClient itemClient;

    @GetMapping
    public ResponseEntity<Object> getItems(@RequestHeader(USER_ID) Long userId,
                                           @PositiveOrZero @RequestParam(defaultValue = "0") Long from,
                                           @Positive @RequestParam(defaultValue = "10") Long size) {
        return itemClient.getItems(userId, from, size);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getItemById(@PathVariable("id") Long id,
                                              @RequestHeader(USER_ID) Long userId) {
        return itemClient.getItemById(userId, id);
    }

    @PostMapping
    public ResponseEntity<Object> createItem(@RequestHeader(USER_ID) Long userId,
                                             @RequestBody @Valid CreateItemDto createItemDto) {
        return itemClient.createItem(userId, createItemDto);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Object> updateItem(@PathVariable("id") Long id,
                                             @RequestHeader(USER_ID) Long userId,
                                             @RequestBody @Valid UpdateItemDto updateItemDto) {
        return itemClient.updateItem(userId, id, updateItemDto);
    }

    @GetMapping("/search")
    public ResponseEntity<Object> getItemsByText(@RequestParam(required = false) String text,
                                                 @RequestHeader(USER_ID) Long userId,
                                                 @PositiveOrZero @RequestParam(defaultValue = "0") Long from,
                                                 @Positive @RequestParam(defaultValue = "10") Long size) {
        return itemClient.getItemsByText(userId, text, from, size);
    }

    @PostMapping("/{id}/comment")
    public ResponseEntity<Object> createComment(@PathVariable("id") Long id,
                                                @RequestBody @Valid CreateCommentDto createCommentDto,
                                                @RequestHeader(USER_ID) Long userId) {
        return itemClient.createComment(userId, id, createCommentDto);
    }
}
