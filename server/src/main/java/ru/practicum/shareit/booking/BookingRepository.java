package ru.practicum.shareit.booking;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.user.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface BookingRepository extends JpaRepository<Booking, Long> {
    @Query(
            value = """
                SELECT b
                FROM Booking b
                where b.id = :id and (b.booker = :user or b.item.owner = :user)
            """)
    Optional<Booking> findByIdAndBookerOrItemOwner(Long id, User user);

    @Query(
            value = """
                SELECT b
                FROM Booking b
                where b.item = :item and b.booker >= :user and b.status = :status and b.end < :dateTime
                order by b.start desc
                limit 1
            """)
    Optional<Booking> findBookingByBookerAndItemAndStatus(User user, Item item, BookingStatus status, LocalDateTime dateTime);

    List<Booking> findBookingByBookerAndStatus(User user, BookingStatus status);

    List<Booking> findBookingByBooker(User user);

    @Query(
            value = """
                SELECT b
                FROM Booking b
                where b.booker = :user and b.start <= :dateTime and b.end >= :dateTime and b.status = :status
            """)
    List<Booking> findBookingByBookerAndCheckDateCurrent(User user, LocalDateTime dateTime, BookingStatus status);

    @Query(
            value = """
                SELECT b
                FROM Booking b
                where b.booker = :user and b.end < :dateTime
            """)
    List<Booking> findBookingByBookerAndCheckDatePast(User user, LocalDateTime dateTime);

    @Query(
            value = """
                SELECT b
                FROM Booking b
                where b.booker = :user and b.start > :dateTime
            """)
    List<Booking> findBookingByBookerAndCheckDateFuture(User user, LocalDateTime dateTime);

    @Query(
            value = """
                SELECT b.end
                FROM Booking b
                where b.item = :item and b.end < :dateTime and b.status = :status
                order by b.end desc
                limit 1
            """)
    Optional<LocalDateTime> findLastBookingByItem(Item item, LocalDateTime dateTime, BookingStatus status);

    @Query(
            value = """
                SELECT b.start
                FROM Booking b
                where b.item = :item and b.start >= :dateTime and b.status = :status
                order by b.start desc
                limit 1
            """)
    Optional<LocalDateTime> findNextBookingByItem(Item item, LocalDateTime dateTime, BookingStatus status);
}
