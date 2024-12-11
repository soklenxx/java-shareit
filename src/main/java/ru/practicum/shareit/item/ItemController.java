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
import ru.practicum.shareit.comments.Comment;
import ru.practicum.shareit.comments.CommentMapper;
import ru.practicum.shareit.comments.dto.CommentDto;
import ru.practicum.shareit.comments.dto.CreateCommentDto;
import ru.practicum.shareit.item.dto.CreateItemDto;
import ru.practicum.shareit.item.dto.ItemDateBookingDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.UpdateItemDto;

import java.util.List;

import static ru.practicum.shareit.Constants.USER_ID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/items")
public class ItemController {

    private final ItemService itemService;
    private final ItemMapper itemMapper;
    private final CommentMapper commentMapper;

    @GetMapping
    public ResponseEntity<List<ItemDto>> getItems(@RequestHeader(USER_ID) Long userId) {
        return ResponseEntity.ok(itemMapper.toItemDto(itemService.getItems(userId)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ItemDateBookingDto> getItemById(@PathVariable("id") Long id) {
        return ResponseEntity.ok(itemMapper.toItemDateBookingDto(itemService.getItemById(id)));
    }

    @PostMapping
    public ResponseEntity<ItemDto> createItem(@RequestBody CreateItemDto createItemDto,
                                              @RequestHeader(USER_ID) Long userId) {
        Item item = itemMapper.toEntity(createItemDto);
        return ResponseEntity.ok(itemMapper.toItemDto(itemService.createItem(item, userId)));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ItemDto> updateItem(@PathVariable("id") Long id,
                                              @RequestHeader(USER_ID) Long userId,
                                              @RequestBody UpdateItemDto updateItemDto) {
        Item item = itemMapper.updateEntity(updateItemDto, id);
        return ResponseEntity.ok(itemMapper.toItemDto(itemService.updateItem(id, userId, item)));
    }

    @GetMapping("/search")
    public ResponseEntity<List<ItemDto>> getItemsByText(@RequestParam(required = false) String text) {
        return ResponseEntity.ok(itemMapper.toItemDto(itemService.getItemsByText(text)));
    }

    @PostMapping("/{id}/comment")
    public ResponseEntity<CommentDto> createComment(@PathVariable("id") Long id,
                                                    @RequestBody CreateCommentDto createCommentDto,
                                                    @RequestHeader(USER_ID) Long userId) {
        Comment comment = commentMapper.toEntity(createCommentDto);
        return ResponseEntity.ok(commentMapper.toCommentDto(itemService.createComment(comment, id, userId)));
    }
}
