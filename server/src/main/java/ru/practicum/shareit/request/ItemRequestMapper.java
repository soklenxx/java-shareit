package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.ItemMapper;
import ru.practicum.shareit.item.ItemService;
import ru.practicum.shareit.request.dto.CreateItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.user.UserMapper;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ItemRequestMapper {
    private final UserMapper userMapper;
    private final ItemService itemService;
    private final ItemMapper itemMapper;

    public ItemRequestDto toItemRequestDto(ItemRequest request) {
        return ItemRequestDto.builder()
                .id(request.getId())
                .description(request.getDescription())
                .requestor(userMapper.toUserDto(request.getRequestor()))
                .created(request.getCreated())
                .items(itemMapper.toItemDto(itemService.getItemByRequestId(request.getId())))
                .build();
    }

    public ItemRequest toEntity(CreateItemRequestDto createItemRequestDto) {
        return ItemRequest.builder()
                .description(createItemRequestDto.getDescription())
                .build();
    }

    public List<ItemRequestDto> toItemRequestDto(List<ItemRequest> items) {
        return items.stream()
                .map(this::toItemRequestDto)
                .toList();
    }
}
