package ru.practicum.shareit.booking;

import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.user.User;

import java.time.LocalDate;

@Data
@Builder
public class Booking {
    private Long id;
    private LocalDate startDate;
    private LocalDate endDate;
    private Item item;
    private User booker;
    private boolean booked;
    private String reviews;
}
