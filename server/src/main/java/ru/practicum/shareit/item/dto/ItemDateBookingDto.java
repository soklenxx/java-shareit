package ru.practicum.shareit.item.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.comments.dto.CommentDto;
import ru.practicum.shareit.user.dto.UserDto;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ItemDateBookingDto {
    private Long id;
    private String name;
    private String description;
    private Boolean available;
    private UserDto owner;
    private LocalDateTime lastBooking;
    private LocalDateTime nextBooking;
    private Long requestId;
    private List<CommentDto> comments;
}
