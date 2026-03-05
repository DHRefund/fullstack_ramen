package com.sapporo.ramenbooking.controller;

import com.sapporo.ramenbooking.dto.request.BookingRequest;
import com.sapporo.ramenbooking.dto.request.CancelBookingRequest;
import com.sapporo.ramenbooking.dto.response.BookingResponse;
import com.sapporo.ramenbooking.dto.response.BookingDetailResponse;
import com.sapporo.ramenbooking.dto.response.CancelBookingResponse;
import com.sapporo.ramenbooking.dto.response.ScheduleResponse;
import com.sapporo.ramenbooking.service.BookingService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000")
public class BookingController {

    private final BookingService bookingService;

    @GetMapping("/schedule")
    public ResponseEntity<ScheduleResponse> getSchedule(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {

        return ResponseEntity.ok(bookingService.getSchedule(date));
    }

    @PostMapping("/reservations")
    public ResponseEntity<BookingResponse> createBooking(
            @Valid @RequestBody BookingRequest request) {

        return ResponseEntity.ok(bookingService.createBooking(request));
    }

    @GetMapping("/reservations/search")
    public ResponseEntity<List<BookingResponse>> searchReservations(
            @RequestParam String phone) {

        return ResponseEntity.ok(
                bookingService.findReservationsByPhone(phone));
    }

    @PostMapping("/reservations/cancel")
    public ResponseEntity<CancelBookingResponse> cancelBooking(
            @Valid @RequestBody CancelBookingRequest request) {

        return ResponseEntity.ok(
                bookingService.cancelBookingByCustomer(request));
    }

    @GetMapping("/reservations/{id}")
    public ResponseEntity<BookingDetailResponse> getBookingById(
            @PathVariable String id) {

        return ResponseEntity.ok(
                bookingService.getBookingById(id));
    }
}