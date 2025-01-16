package ru.practicum.shareit.request;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.shareit.request.dto.CreateItemRequestDto;

import static ru.practicum.shareit.Constants.USER_ID;

@RestController
@Validated
@RequiredArgsConstructor
@RequestMapping(path = "/requests")
public class ItemRequestController {
    private final ItemRequestClient itemRequestClient;

    @GetMapping
    public ResponseEntity<Object> getRequests(@RequestHeader(USER_ID) Long userId) {
        return itemRequestClient.getRequests(userId);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getRequestById(@PathVariable("id") Long id,
                                                 @RequestHeader(USER_ID) Long userId) {
        return itemRequestClient.get(userId, id);
    }

    @PostMapping
    public ResponseEntity<Object> createItem(@RequestBody @Valid CreateItemRequestDto createItemRequestDto,
                                             @RequestHeader(USER_ID) Long userId) {
        return itemRequestClient.createItem(userId, createItemRequestDto);
    }
}
