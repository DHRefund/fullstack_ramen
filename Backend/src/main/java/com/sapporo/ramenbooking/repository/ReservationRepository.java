package com.sapporo.ramenbooking.repository;

import com.sapporo.ramenbooking.entity.Reservation;
import com.sapporo.ramenbooking.entity.ReservationStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.time.LocalDate;
import java.util.List;

public interface ReservationRepository extends JpaRepository<Reservation, String> {

    List<Reservation> findByBookingDateOrderByTimeSlotAsc(LocalDate date);

    List<Reservation> findByBookingDateAndStatus(LocalDate date, ReservationStatus status);

    @Query("SELECT COUNT(r) FROM Reservation r WHERE r.bookingDate = :date AND r.timeSlot = :timeSlot AND r.status = :status")
    Long countByDateAndTimeSlotAndStatus(
            @Param("date") LocalDate date,
            @Param("timeSlot") String timeSlot,
            @Param("status") ReservationStatus status);

    List<Reservation> findByCustomerPhoneAndStatusNot(String customerPhone, ReservationStatus status);
}