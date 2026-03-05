package com.sapporo.ramenbooking.service;

import com.sapporo.ramenbooking.dto.request.BookingRequest;
import com.sapporo.ramenbooking.dto.response.BookingResponse;
import com.sapporo.ramenbooking.dto.response.ScheduleResponse;
import com.sapporo.ramenbooking.dto.request.CancelBookingRequest;
import com.sapporo.ramenbooking.dto.response.CancelBookingResponse;
import com.sapporo.ramenbooking.dto.response.BookingDetailResponse;
import java.time.LocalDate;
import java.util.List;

public interface BookingService {
    ScheduleResponse getSchedule(LocalDate date);

    BookingResponse createBooking(BookingRequest request);

    List<BookingResponse> getAllReservationsByDate(LocalDate date);

    List<BookingResponse> getAllReservations();

    BookingResponse cancelReservation(String reservationId);

    BookingResponse completeReservation(String reservationId);

    List<BookingResponse> findReservationsByPhone(String customerPhone);

    CancelBookingResponse cancelBookingByCustomer(CancelBookingRequest request);

    BookingDetailResponse getBookingById(String bookingId);
}