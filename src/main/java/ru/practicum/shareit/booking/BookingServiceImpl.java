package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.dto.CreateBookingDto;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.exceptions.ValidationException;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.item.ItemService;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserService;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {
    private final BookingRepository bookingRepository;
    private final BookingMapper bookingMapper;
    private final ItemService itemService;
    private final UserService userService;

    @Override
    public Booking createBooking(CreateBookingDto createBookingDto, Long userId) {
        User user = userService.getUserById(userId);
        Item item = itemService.getItemById(createBookingDto.getItemId());
        Booking booking = bookingMapper.toEntity(createBookingDto);
        validate(booking, item);
        booking.setStatus(BookingStatus.WAITING);
        booking.setBooker(user);
        booking.setItem(item);
        return bookingRepository.save(booking);
    }

    @Override
    public Booking approvedBookingRequest(Long id, Boolean approved, Long userId) {
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Booking not found"));
        if (!booking.getItem().getOwner().getId().equals(userId)) {
            throw new ValidationException("Request can only be completed by the owner of the item");
        }
        if (approved) {
            booking.setStatus(BookingStatus.APPROVED);
        } else {
            booking.setStatus(BookingStatus.REJECTED);
        }
        return bookingRepository.save(booking);
    }

    @Override
    public Booking getBookingById(Long id, Long userId) {
        User user = userService.getUserById(userId);
        return bookingRepository.findByIdAndBookerOrItemOwner(id, user)
                .orElseThrow(() -> new NotFoundException("Booking not found"));
    }

    @Override
    public List<Booking> getBookingByUser(Long userId, String state) {
        BookingState stateValue = BookingState.valueOf(state);
        User user = userService.getUserById(userId);
        LocalDateTime now = LocalDateTime.now();
        if (stateValue.equals(BookingState.ALL)) {
            return bookingRepository.findBookingByBooker(user);
        }
        if (stateValue.equals(BookingState.CURRENT)) {
            return bookingRepository.findBookingByBookerAndCheckDateCurrent(user, now, BookingStatus.APPROVED);
        }

        if (stateValue.equals(BookingState.PAST)) {
            return bookingRepository.findBookingByBookerAndCheckDatePast(user, now);
        }

        if (stateValue.equals(BookingState.FUTURE)) {
            return bookingRepository.findBookingByBookerAndCheckDateFuture(user, now);
        }

        if (stateValue.equals(BookingState.WAITING)) {
            return bookingRepository.findBookingByBookerAndStatus(user, BookingStatus.WAITING);
        }

        if (stateValue.equals(BookingState.REJECTED)) {
            return bookingRepository.findBookingByBookerAndStatus(user, BookingStatus.REJECTED);
        }

        return List.of();

    }

    private void validate(Booking booking, Item item) {
        if (booking.getEnd().isBefore(LocalDateTime.now())) {
            throw new ValidationException("Booking ends in past");
        }

        if (booking.getEnd().isEqual(booking.getStart())) {
            throw new ValidationException("Booking ends equal start");
        }

        if (booking.getEnd() == null || booking.getStart() == null) {
            throw new ValidationException("Booking dates cannot be null ");
        }

        if (!item.getAvailable()) {
            throw new ValidationException("Booking item is unavailable");
        }
    }
}
