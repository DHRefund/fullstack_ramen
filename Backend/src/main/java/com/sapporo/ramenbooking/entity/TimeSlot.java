package com.sapporo.ramenbooking.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;

@Entity
@Table(name = "time_slots", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"date", "time"})
})
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TimeSlot {
    
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    
    @Column(nullable = false)
    private LocalDate date;
    
    @Column(nullable = false, length = 10)
    private String time;
    
    @Column(nullable = false)
    private Integer maxCapacity;
    
    @Column(nullable = false)
    private Integer bookedCount = 0;
    
    @Column(nullable = false)
    private Boolean available = true;
}