package com.sapporo.ramenbooking.dto.response;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TimeSlotResponse {
    
    private String id;
    private String time;
    private Integer maxCapacity;
    private Integer bookedCount;
    private Boolean available;
}