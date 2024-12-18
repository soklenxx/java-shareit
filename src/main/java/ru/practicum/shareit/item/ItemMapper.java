package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.comments.CommentMapper;
import ru.practicum.shareit.item.dto.CreateItemDto;
import ru.practicum.shareit.item.dto.ItemDateBookingDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.UpdateItemDto;
import ru.practicum.shareit.user.UserMapper;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ItemMapper {
    private final ItemService itemService;
    private final CommentMapper commentMapper;
    private final UserMapper userMapper;

    public ItemDto toItemDto(Item item) {
        return ItemDto.builder()
                .id(item.getId())
                .name(item.getName())
                .description(item.getDescription())
                .available(item.getAvailable())
                .owner(userMapper.toUserDto(item.getOwner()))
                .comments(commentMapper.toCommentDto(itemService.getComments(item.getId())))
                .build();
    }

    public Item toEntity(CreateItemDto createItemDto) {
        return Item.builder()
                .name(createItemDto.getName())
                .description(createItemDto.getDescription())
                .available(createItemDto.getAvailable())
                .build();
    }

    public Item updateEntity(UpdateItemDto updateItemDto, Long id) {
        Item item = itemService.getItemById(id);
        return Item.builder()
                .id(item.getId())
                .name(updateItemDto.getName() != null ? updateItemDto.getName() : item.getName())
                .description(updateItemDto.getDescription() != null ? updateItemDto.getDescription() : item.getDescription())
                .available(updateItemDto.getAvailable() != null ? updateItemDto.getAvailable() : item.getAvailable())
                .owner(item.getOwner())
                .request(item.getRequest())
                .build();
    }

    public ItemDateBookingDto toItemDateBookingDto(Item item) {
        return ItemDateBookingDto.builder()
                .id(item.getId())
                .name(item.getName())
                .description(item.getDescription())
                .lastBooking(itemService.findLastBookingByItem(item))
                .nextBooking(itemService.findNextBookingByItem(item))
                .available(item.getAvailable())
                .owner(userMapper.toUserDto(item.getOwner()))
                .comments(commentMapper.toCommentDto(itemService.getComments(item.getId())))
                .build();
    }

    public List<ItemDto> toItemDto(List<Item> items) {
        return items.stream()
                .map(this::toItemDto)
                .toList();
    }
}
