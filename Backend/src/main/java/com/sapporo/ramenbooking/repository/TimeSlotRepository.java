package com.sapporo.ramenbooking.repository;

import com.sapporo.ramenbooking.entity.TimeSlot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface TimeSlotRepository extends JpaRepository<TimeSlot, String> {
    
    List<TimeSlot> findByDateOrderByTimeAsc(LocalDate date);
    
    Optional<TimeSlot> findByDateAndTime(LocalDate date, String time);
    
    boolean existsByDateAndTime(LocalDate date, String time);

    @Modifying
    @Query("""
        UPDATE TimeSlot t
        SET t.bookedCount = t.bookedCount + 1,
            t.available = CASE 
                WHEN t.bookedCount + 1 >= t.maxCapacity THEN false 
                ELSE true 
            END
        WHERE t.id = :id
          AND t.available = true
          AND t.bookedCount < t.maxCapacity
        """)
    int bookIfAvailable(@Param("id") String id);
}