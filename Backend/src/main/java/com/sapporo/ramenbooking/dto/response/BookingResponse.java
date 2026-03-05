package com.sapporo.ramenbooking.dto.response;

import lombok.*;
import java.time.LocalDateTime;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookingResponse {

    private LocalDate bookingDate;
    private String bookingId;
    private String status;
    private LocalDateTime createdAt;
    private String message;
    private String customerName;
    private String customerPhone;
    private Integer numberOfGuests;
    private String timeSlot;
}