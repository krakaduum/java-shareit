package ru.practicum.shareit.booking.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Status;

import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    List<Booking> findAllByBookerIdOrderByStartDesc(Long bookerId);

    List<Booking> findAllByBookerIdAndStatusOrderByStartDesc(Long bookerId, Status status);

    @Query("SELECT b " +
            "FROM Booking as b " +
            "WHERE b.booker.id = ?1 " +
            "AND b.status IN ('WAITING', 'APPROVED') " +
            "AND b.start > CURRENT_TIMESTAMP " +
            "ORDER BY b.start DESC"
    )
    List<Booking> findAllFutureBookingsByBookerIdOrderByStartDesc(Long bookerId);

    @Query("SELECT b " +
            "FROM Booking b " +
            "WHERE b.booker.id = ?1 " +
            "AND b.status = 'APPROVED' " +
            "AND b.end < CURRENT_TIMESTAMP " +
            "ORDER BY b.start DESC"
    )
    List<Booking> findAllPastBookingsByBookerIdOrderByStartDesc(Long bookerId);

    @Query("SELECT b " +
            "FROM Booking b " +
            "WHERE b.booker.id = ?1 " +
            "AND b.status = 'APPROVED' " +
            "AND (b.start < CURRENT_TIMESTAMP AND b.end > CURRENT_TIMESTAMP) " +
            "ORDER BY b.start DESC"
    )
    List<Booking> findAllCurrentBookingsByBookerIdOrderByStartDesc(Long bookerId);

    List<Booking> findAllByItemOwnerIdOrderByStartDesc(Long ownerId);

    List<Booking> findAllByItemOwnerIdAndStatusOrderByStartDesc(Long ownerId, Status status);

    @Query("SELECT b " +
            "FROM Booking as b " +
            "WHERE b.item.owner.id = ?1 " +
            "AND b.status IN ('WAITING', 'APPROVED') " +
            "AND b.start > CURRENT_TIMESTAMP " +
            "ORDER BY b.start DESC"
    )
    List<Booking> findAllFutureBookingsByItemOwnerIdOrderByStartDesc(Long ownerId);

    @Query("SELECT b " +
            "FROM Booking b " +
            "WHERE b.item.owner.id = ?1 " +
            "AND b.status = 'APPROVED' " +
            "AND b.end < CURRENT_TIMESTAMP " +
            "ORDER BY b.start DESC"
    )
    List<Booking> findAllPastBookingsByItemOwnerIdOrderByStartDesc(Long ownerId);

    @Query("SELECT b " +
            "FROM Booking b " +
            "WHERE b.item.owner.id = ?1 " +
            "AND b.status = 'APPROVED' " +
            "AND (b.start < CURRENT_TIMESTAMP AND b.end > CURRENT_TIMESTAMP) " +
            "ORDER BY b.start DESC"
    )
    List<Booking> findAllCurrentBookingsByItemOwnerIdOrderByStartDesc(Long ownerId);

    @Query("SELECT b " +
            "FROM Booking b " +
            "WHERE b.item.id = ?1 " +
            "AND b.status = 'APPROVED' " +
            "AND b.start < CURRENT_TIMESTAMP " +
            "ORDER BY b.start DESC"
    )
    List<Booking> findAllCurrentOrPastBookingsByItemIdOrderByStartDesc(Long itemId);

    @Query("SELECT b " +
            "FROM Booking as b " +
            "WHERE b.item.id = ?1 " +
            "AND b.status IN ('WAITING', 'APPROVED') " +
            "AND b.start > CURRENT_TIMESTAMP " +
            "ORDER BY b.start DESC"
    )
    List<Booking> findAllFutureBookingsByItemIdOrderByStartDesc(Long itemId);

}
