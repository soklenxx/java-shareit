
package ru.practicum.shareit.booking;

import lombok.Getter;

@Getter
public enum BookingState {
    ALL,
    CURRENT,
    PAST,
    FUTURE,
    WAITING,
    REJECTED
}
