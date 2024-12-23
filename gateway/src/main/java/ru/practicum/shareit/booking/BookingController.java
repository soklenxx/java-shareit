package ru.practicum.shareit.booking;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.shareit.booking.dto.CreateBookingDto;

import static ru.practicum.shareit.Constants.USER_ID;

@RestController
@RequiredArgsConstructor
@Slf4j
@Validated
@RequestMapping(path = "/bookings")
public class BookingController {
    private final BookingClient bookingClient;

    @PostMapping
    public ResponseEntity<Object> createBooking(@RequestBody @Valid CreateBookingDto createBookingDto,
                                                @RequestHeader(USER_ID) Long userId) {
        log.info("Creating booking {}, userId={}", createBookingDto, userId);
        return bookingClient.createBooking(userId, createBookingDto);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getBookingById(@PathVariable("id") Long id,
                                                 @RequestHeader(USER_ID) Long userId) {
        log.info("Get booking {}, userId={}", id, userId);
        return bookingClient.getBookingById(userId, id);

    }

    @PatchMapping("/{id}")
    public ResponseEntity<Object> approvedBookingRequest(@PathVariable("id") Long id,
                                                         @RequestParam(required = false) Boolean approved,
                                                         @RequestHeader(USER_ID) Long userId) {
        log.info("Approved booking {}, userId={}", id, userId);
        return bookingClient.approve(userId, id, approved);
    }

    @GetMapping
    public ResponseEntity<Object> get(@RequestHeader(USER_ID) Long userId,
                                      @RequestParam(name = "state", defaultValue = "ALL") String stateParam,
                                      @PositiveOrZero @RequestParam(name = "from", defaultValue = "0") Long from,
                                      @Positive @RequestParam(name = "size", defaultValue = "10") Long size) {
        BookingState state = BookingState.from(stateParam)
                .orElseThrow(() -> new IllegalArgumentException("Unknown state: " + stateParam));
        return bookingClient.getBookings(userId, state, from, size);
    }

    @GetMapping("/owner")
    public ResponseEntity<Object> getBookingByUser(@RequestParam(name = "state", defaultValue = "ALL") String stateParam,
                                                   @PositiveOrZero @RequestParam(name = "from", defaultValue = "0") Integer from,
                                                   @Positive @RequestParam(name = "size", defaultValue = "10") Integer size,
                                                   @RequestHeader(USER_ID) Long userId) {
        BookingState state = BookingState.from(stateParam)
                .orElseThrow(() -> new IllegalArgumentException("Unknown state: " + stateParam));

        log.info("Get booking with state {}, userId={}, from={}, size={}", stateParam, userId, from, size);
        return bookingClient.getBookingByUser(userId, state, from, size);
    }
}
