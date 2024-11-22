package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.dto.CreateItemDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.UpdateItemDto;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ItemMapper {
    private final ItemService itemService;

    public ItemDto toItemDto(Item item) {
        return ru.practicum.shareit.item.dto.ItemDto.builder()
                .id(item.getId())
                .name(item.getName())
                .description(item.getDescription())
                .available(item.getAvailable())
                .requestId(item.getRequest() != null ? item.getRequest().getId() : null)
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
                .build();
    }

    public List<ItemDto> toItemDto(List<Item> items) {
        return items.stream()
                .map(this::toItemDto)
                .toList();
    }
}
