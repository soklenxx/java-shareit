package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.CreateBookingDto;
import ru.practicum.shareit.item.ItemMapper;
import ru.practicum.shareit.user.UserMapper;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BookingMapper {
    private final ItemMapper itemMapper;
    private final UserMapper userMapper;

    public BookingDto toBookingDto(Booking booking) {
        return BookingDto.builder()
                .id(booking.getId())
                .start(booking.getStart())
                .end(booking.getEnd())
                .item(itemMapper.toItemDto(booking.getItem()))
                .booker(userMapper.toUserDto(booking.getBooker()))
                .status(booking.getStatus().toString())
                .build();
    }

    public Booking toEntity(CreateBookingDto createBookingDto) {
        return Booking.builder()
                .start(createBookingDto.getStart())
                .end(createBookingDto.getEnd())
                .build();
    }

    public List<BookingDto> toBookingDto(List<Booking> items) {
        return items.stream()
                .map(this::toBookingDto)
                .toList();
    }
}
