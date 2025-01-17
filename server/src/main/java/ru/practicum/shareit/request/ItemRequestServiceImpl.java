package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserService;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ItemRequestServiceImpl implements ItemRequestService {
    private final ItemRequestRepository itemRequestRepository;
    private final UserService userService;

    @Override
    public List<ItemRequest> getRequests(Long userId) {
        log.info("Request to get Items");
        return itemRequestRepository.findByRequestorId(userId);
    }

    @Override
    public ItemRequest createItemRequest(@RequestBody ItemRequest request, Long userId) {
        log.info("Request to create Item");
        User user = userService.getUserById(userId);
        request.setRequestor(user);
        return itemRequestRepository.save(request);
    }

    @Override
    public ItemRequest getItemRequestById(Long id) {
        return itemRequestRepository.findById(id).orElseThrow(() ->  new NotFoundException("Запрос с id = " + id + " не найден"));
    }
}
