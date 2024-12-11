package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.CreateBookingDto;

import java.util.List;

import static ru.practicum.shareit.Constants.USER_ID;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/bookings")
public class BookingController {
    private final BookingMapper bookingMapper;
    private final BookingService bookingService;

    @PostMapping
    public ResponseEntity<BookingDto> createBooking(@RequestBody CreateBookingDto createBookingDto,
                                                    @RequestHeader(USER_ID) Long userId) {
        return ResponseEntity.ok(bookingMapper.toBookingDto(bookingService.createBooking(createBookingDto, userId)));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<BookingDto> approvedBookingRequest(@PathVariable("id") Long id,
                                                             @RequestParam(required = false) Boolean approved,
                                                             @RequestHeader(USER_ID) Long userId) {
        return ResponseEntity.ok(bookingMapper.toBookingDto(bookingService.approvedBookingRequest(id, approved, userId)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<BookingDto> getBookingById(@PathVariable("id") Long id,
                                                     @RequestHeader(USER_ID) Long userId) {
        return ResponseEntity.ok(bookingMapper.toBookingDto(bookingService.getBookingById(id, userId)));
    }

    @GetMapping
    public ResponseEntity<List<BookingDto>> getBookingByUser(@RequestParam(required = false, defaultValue = "ALL") String state,
                                                             @RequestHeader(USER_ID) Long userId) {
        return ResponseEntity.ok(bookingMapper.toBookingDto(bookingService.getBookingByUser(userId, state)));
    }
}
