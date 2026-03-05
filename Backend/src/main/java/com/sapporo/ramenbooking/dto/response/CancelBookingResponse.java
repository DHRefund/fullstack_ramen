package com.sapporo.ramenbooking.dto.response;

import lombok.*;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CancelBookingResponse {

    private String bookingId;
    private String status;
    private String message;
    private LocalDateTime cancelledAt;
    private Boolean refundEligible;
}