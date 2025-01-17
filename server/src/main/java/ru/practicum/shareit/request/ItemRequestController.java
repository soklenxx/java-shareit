package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.shareit.request.dto.CreateItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestDto;

import java.util.List;

import static ru.practicum.shareit.Constants.USER_ID;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/requests")
public class ItemRequestController {
    private final ItemRequestService itemRequestService;
    private final ItemRequestMapper itemRequestMapper;

    @GetMapping
    public ResponseEntity<List<ItemRequestDto>> getRequests(@RequestHeader(USER_ID) Long userId) {
        return ResponseEntity.ok(itemRequestMapper.toItemRequestDto(itemRequestService.getRequests(userId)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ItemRequestDto> getRequestById(@PathVariable("id") Long id) {
        return ResponseEntity.ok(itemRequestMapper.toItemRequestDto(itemRequestService.getItemRequestById(id)));
    }

    @PostMapping
    public ResponseEntity<ItemRequestDto> createItem(@RequestBody CreateItemRequestDto createItemRequestDto,
                                                     @RequestHeader(USER_ID) Long userId) {
        ItemRequest request = itemRequestMapper.toEntity(createItemRequestDto);
        return ResponseEntity.ok(itemRequestMapper.toItemRequestDto(itemRequestService.createItemRequest(request, userId)));
    }
}
