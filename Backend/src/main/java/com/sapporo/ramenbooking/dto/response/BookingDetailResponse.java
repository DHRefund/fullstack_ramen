package com.sapporo.ramenbooking.dto.response;

import lombok.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookingDetailResponse {

    private String bookingId;
    private String customerName;
    private String customerPhone;
    private String customerEmail;
    private LocalDate bookingDate;
    private String timeSlot;
    private Integer numberOfGuests;
    private String status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}