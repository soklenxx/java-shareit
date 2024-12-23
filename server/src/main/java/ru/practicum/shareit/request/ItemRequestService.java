package ru.practicum.shareit.request;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ItemRequestService {
    List<ItemRequest> getRequests(Long userId);

    ItemRequest getItemRequestById(Long id);

    ItemRequest createItemRequest(ItemRequest request, Long userId);
}
