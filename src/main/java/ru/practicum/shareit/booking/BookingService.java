package ru.practicum.shareit.booking;

import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.dto.CreateBookingDto;

import java.util.List;

@Service
public interface BookingService {
    Booking createBooking(CreateBookingDto createBookingDto, Long userId);

    Booking approvedBookingRequest(Long id, Boolean approved, Long userId);

    Booking getBookingById(Long id, Long userId);

    List<Booking> getBookingByUser(Long userId, String state);

}
