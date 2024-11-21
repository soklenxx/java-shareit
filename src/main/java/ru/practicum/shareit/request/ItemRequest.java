package ru.practicum.shareit.request;

import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.user.User;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * TODO Sprint add-item-requests.
 */
@Data
@Builder
public class ItemRequest {
    private Long id;
    private String name;
    private String description;
    private User requestor;
    private LocalDateTime created;
}
