package ru.practicum.shareit.booking.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Status;

import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    Page<Booking> findAllByBookerIdOrderByStartDesc(Long bookerId, Pageable pageable);

    Page<Booking> findAllByBookerIdAndStatusOrderByIdAsc(Long bookerId, Status status, Pageable pageable);

    @Query("SELECT b " +
            "FROM Booking as b " +
            "WHERE b.booker.id = ?1 " +
            "AND b.status IN ('WAITING', 'APPROVED') " +
            "AND b.start > CURRENT_TIMESTAMP " +
            "ORDER BY b.start DESC"
    )
    Page<Booking> findAllFutureBookingsByBookerIdOrderByStartDesc(Long bookerId, Pageable pageable);

    @Query("SELECT b " +
            "FROM Booking b " +
            "WHERE b.booker.id = ?1 " +
            "AND b.status = 'APPROVED' " +
            "AND b.end < CURRENT_TIMESTAMP " +
            "ORDER BY b.start DESC"
    )
    Page<Booking> findAllPastBookingsByBookerIdOrderByStartDesc(Long bookerId, Pageable pageable);

    @Query("SELECT b " +
            "FROM Booking b " +
            "WHERE b.booker.id = ?1 " +
            "AND b.status IN ('REJECTED', 'APPROVED') " +
            "AND (b.start < CURRENT_TIMESTAMP AND b.end > CURRENT_TIMESTAMP) " +
            "ORDER BY b.id ASC"
    )
    Page<Booking> findAllCurrentBookingsByBookerIdOrderByIdAsc(Long bookerId, Pageable pageable);

    Page<Booking> findAllByItemOwnerIdOrderByStartDesc(Long ownerId, Pageable pageable);

    Page<Booking> findAllByItemOwnerIdAndStatusOrderByIdAsc(Long ownerId, Status status, Pageable pageable);

    @Query("SELECT b " +
            "FROM Booking as b " +
            "WHERE b.item.owner.id = ?1 " +
            "AND b.status IN ('WAITING', 'APPROVED') " +
            "AND b.start > CURRENT_TIMESTAMP " +
            "ORDER BY b.start DESC"
    )
    Page<Booking> findAllFutureBookingsByItemOwnerIdOrderByStartDesc(Long ownerId, Pageable pageable);

    @Query("SELECT b " +
            "FROM Booking b " +
            "WHERE b.item.owner.id = ?1 " +
            "AND b.status = 'APPROVED' " +
            "AND b.end < CURRENT_TIMESTAMP " +
            "ORDER BY b.start DESC"
    )
    Page<Booking> findAllPastBookingsByItemOwnerIdOrderByStartDesc(Long ownerId, Pageable pageable);

    @Query("SELECT b " +
            "FROM Booking b " +
            "WHERE b.item.owner.id = ?1 " +
            "AND b.status IN ('REJECTED', 'APPROVED') " +
            "AND (b.start < CURRENT_TIMESTAMP AND b.end > CURRENT_TIMESTAMP) " +
            "ORDER BY b.id ASC"
    )
    Page<Booking> findAllCurrentBookingsByItemOwnerIdOrderByIdAsc(Long ownerId, Pageable pageable);

    @Query("SELECT b " +
            "FROM Booking b " +
            "WHERE b.item.id = ?1 " +
            "AND b.status = 'APPROVED' " +
            "AND b.start < CURRENT_TIMESTAMP " +
            "ORDER BY b.start ASC"
    )
    List<Booking> findAllCurrentOrPastBookingsByItemIdOrderByStartAsc(Long itemId);

    @Query("SELECT b " +
            "FROM Booking as b " +
            "WHERE b.item.id = ?1 " +
            "AND b.status IN ('WAITING', 'APPROVED') " +
            "AND b.start > CURRENT_TIMESTAMP " +
            "ORDER BY b.start DESC"
    )
    List<Booking> findAllFutureBookingsByItemIdOrderByStartDesc(Long itemId);

    @Query("SELECT b " +
            "FROM Booking b " +
            "WHERE b.booker.id = ?1 " +
            "AND b.item.id = ?2 " +
            "AND b.status = 'APPROVED' " +
            "AND b.end < CURRENT_TIMESTAMP " +
            "ORDER BY b.id ASC"
    )
    List<Booking> findAllPastBookingsByBookerIdAndItemIdOrderByIdAsc(Long bookerId, Long itemId);

}
