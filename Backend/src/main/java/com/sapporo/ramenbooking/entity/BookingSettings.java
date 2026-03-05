package com.sapporo.ramenbooking.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "booking_settings")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookingSettings {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer defaultMaxCapacity = 10;

    private Integer maxAdvanceBookingDays = 30;

    private Integer minCancelHours = 2;

    private String openingHours = "11:00-14:00,18:00-22:00";
}