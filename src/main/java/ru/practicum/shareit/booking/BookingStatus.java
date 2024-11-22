package ru.practicum.shareit.booking;

import lombok.Getter;

@Getter
public enum BookingStatus {
    WAITING,
    APPROVED,
    REJECTED,
    CANCELED
}
