package com.sapporo.ramenbooking.dto.response;

import lombok.*;
import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ScheduleResponse {
    
    private LocalDate date;
    private List<TimeSlotResponse> slots;
}