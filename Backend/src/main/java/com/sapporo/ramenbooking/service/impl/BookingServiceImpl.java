package com.sapporo.ramenbooking.service.impl;

import com.sapporo.ramenbooking.dto.request.BookingRequest;
import com.sapporo.ramenbooking.dto.request.CancelBookingRequest;
import com.sapporo.ramenbooking.dto.response.*;
import com.sapporo.ramenbooking.entity.*;
import com.sapporo.ramenbooking.exception.BookingException;
import com.sapporo.ramenbooking.repository.ReservationRepository;
import com.sapporo.ramenbooking.repository.TimeSlotRepository;
import com.sapporo.ramenbooking.service.BookingService;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.*;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {

    private final ReservationRepository reservationRepository;
    private final TimeSlotRepository timeSlotRepository;

    @Override
    @Transactional
    public ScheduleResponse getSchedule(LocalDate date) {
        validateBookingDate(date);

        List<TimeSlot> slots = getOrCreateTimeSlots(date);

        List<TimeSlotResponse> slotResponses = slots.stream()
                .map(slot -> TimeSlotResponse.builder()
                        .id(slot.getId())
                        .time(slot.getTime())
                        .maxCapacity(slot.getMaxCapacity())
                        .bookedCount(slot.getBookedCount())
                        .available(slot.getAvailable())
                        .build())
                .collect(Collectors.toList());

        return ScheduleResponse.builder()
                .date(date)
                .slots(slotResponses)
                .build();
    }

    @Override
    @Transactional
    public BookingResponse createBooking(BookingRequest request) {

        validateBookingDate(request.getBookingDate());

        TimeSlot timeSlot = timeSlotRepository.findById(request.getTimeSlotId())
                .orElseThrow(() ->
                        new BookingException("指定された時間帯が存在しません。"));

        int rowsAffected =
                timeSlotRepository.bookIfAvailable(request.getTimeSlotId());

        if (rowsAffected == 0) {
            throw new BookingException("指定された時間帯は満席です。");
        }

        Reservation reservation = Reservation.builder()
                .customerName(request.getCustomerName())
                .customerPhone(request.getCustomerPhone())
                .customerEmail(request.getCustomerEmail())
                .bookingDate(request.getBookingDate())
                .timeSlot(timeSlot.getTime())
                .numberOfGuests(request.getNumberOfGuests())
                .status(ReservationStatus.BOOKED)
                .build();

        reservation = reservationRepository.save(reservation);

        return BookingResponse.builder()
                .bookingId(reservation.getId())
                .status(reservation.getStatus().name())
                .createdAt(reservation.getCreatedAt())
                .message("ご予約が正常に完了しました。")
                .customerName(reservation.getCustomerName())
                .customerPhone(reservation.getCustomerPhone())
                .numberOfGuests(reservation.getNumberOfGuests())
                .timeSlot(reservation.getTimeSlot())
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public List<BookingResponse> getAllReservations() {
        return reservationRepository.findAll().stream()
                .map(this::toBookingResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<BookingResponse> getAllReservationsByDate(LocalDate date) {
        return reservationRepository
                .findByBookingDateOrderByTimeSlotAsc(date)
                .stream()
                .map(this::toBookingResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public BookingResponse cancelReservation(String reservationId) {

        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() ->
                        new BookingException("ご予約が見つかりません。"));

        if (reservation.getStatus() == ReservationStatus.CANCELLED) {
            throw new BookingException("既にキャンセル済みの予約です。");
        }

        reservation.setStatus(ReservationStatus.CANCELLED);
        reservationRepository.save(reservation);

        timeSlotRepository
                .findByDateAndTime(reservation.getBookingDate(),
                        reservation.getTimeSlot())
                .ifPresent(timeSlot -> {
                    timeSlot.setBookedCount(
                            Math.max(0, timeSlot.getBookedCount() - 1));
                    timeSlot.setAvailable(true);
                    timeSlotRepository.save(timeSlot);
                });

        return BookingResponse.builder()
                .bookingId(reservationId)
                .status(ReservationStatus.CANCELLED.name())
                .message("ご予約のキャンセルが完了しました。")
                .build();
    }

    @Override
    @Transactional
    public BookingResponse completeReservation(String reservationId) {

        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() ->
                        new BookingException("ご予約が見つかりません。"));

        reservation.setStatus(ReservationStatus.COMPLETED);
        reservationRepository.save(reservation);

        return BookingResponse.builder()
                .bookingId(reservationId)
                .status(ReservationStatus.COMPLETED.name())
                .message("ご予約が完了しました。")
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public List<BookingResponse> findReservationsByPhone(String customerPhone) {

        List<Reservation> reservations =
                reservationRepository.findByCustomerPhoneAndStatusNot(
                        customerPhone,
                        ReservationStatus.CANCELLED);

        return reservations.stream()
                .map(r -> {
                    BookingResponse response = toBookingResponse(r);
                    response.setMessage("ご予約が見つかりました。");
                    return response;
                })
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public CancelBookingResponse cancelBookingByCustomer(
            CancelBookingRequest request) {

        Reservation reservation =
                reservationRepository.findById(request.getBookingId())
                        .orElseThrow(() ->
                                new BookingException("ご予約が見つかりません。"));

        if (!reservation.getCustomerPhone()
                .equals(request.getCustomerPhone())) {
            throw new BookingException("電話番号が予約情報と一致しません。");
        }

        if (reservation.getStatus() != ReservationStatus.BOOKED) {
            throw new BookingException("この予約はキャンセルできません。");
        }

        LocalDateTime bookingDateTime =
                LocalDateTime.of(reservation.getBookingDate(),
                        LocalTime.parse(reservation.getTimeSlot()));

        long hoursUntilBooking =
                Duration.between(LocalDateTime.now(),
                        bookingDateTime).toHours();

        if (hoursUntilBooking < 2) {
            throw new BookingException(
                    "予約時間の2時間前までキャンセル可能です。");
        }

        reservation.setStatus(ReservationStatus.CANCELLED);
        reservationRepository.save(reservation);

        timeSlotRepository
                .findByDateAndTime(reservation.getBookingDate(),
                        reservation.getTimeSlot())
                .ifPresent(timeSlot -> {
                    timeSlot.setBookedCount(
                            Math.max(0, timeSlot.getBookedCount() - 1));
                    timeSlot.setAvailable(true);
                    timeSlotRepository.save(timeSlot);
                });

        return CancelBookingResponse.builder()
                .bookingId(reservation.getId())
                .status(ReservationStatus.CANCELLED.name())
                .message("ご予約のキャンセルが完了しました。")
                .cancelledAt(LocalDateTime.now())
                .refundEligible(hoursUntilBooking >= 24)
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public BookingDetailResponse getBookingById(String bookingId) {

        Reservation reservation =
                reservationRepository.findById(bookingId)
                        .orElseThrow(() ->
                                new BookingException("ご予約が見つかりません。"));

        return BookingDetailResponse.builder()
                .bookingId(reservation.getId())
                .customerName(reservation.getCustomerName())
                .customerPhone(reservation.getCustomerPhone())
                .customerEmail(reservation.getCustomerEmail())
                .bookingDate(reservation.getBookingDate())
                .timeSlot(reservation.getTimeSlot())
                .numberOfGuests(reservation.getNumberOfGuests())
                .status(reservation.getStatus().name())
                .createdAt(reservation.getCreatedAt())
                .updatedAt(reservation.getUpdatedAt())
                .build();
    }

    private void validateBookingDate(LocalDate date) {

        LocalDate today = LocalDate.now();
        LocalDate maxDate = today.plusDays(30);

        if (date.isBefore(today)) {
            throw new BookingException("過去の日付は指定できません。");
        }

        if (date.isAfter(maxDate)) {
            throw new BookingException(
                    "予約は最大30日先まで可能です。");
        }
    }

    private List<TimeSlot> getOrCreateTimeSlots(LocalDate date) {

        List<TimeSlot> slots =
                timeSlotRepository.findByDateOrderByTimeAsc(date);

        if (slots.isEmpty()) {
            slots = createDefaultTimeSlots(date);
            timeSlotRepository.saveAll(slots);
        }

        return slots;
    }

    private List<TimeSlot> createDefaultTimeSlots(LocalDate date) {

        String[] times = {
                "11:00", "11:30", "12:00", "12:30",
                "13:00", "13:30",
                "18:00", "18:30", "19:00",
                "19:30", "20:00", "20:30"
        };

        return java.util.Arrays.stream(times)
                .map(time -> TimeSlot.builder()
                        .date(date)
                        .time(time)
                        .maxCapacity(10)
                        .bookedCount(0)
                        .available(true)
                        .build())
                .collect(Collectors.toList());
    }

    private BookingResponse toBookingResponse(Reservation r) {
        return BookingResponse.builder()
                .bookingId(r.getId())
                .status(r.getStatus().name())
                .createdAt(r.getCreatedAt())
                .bookingDate(r.getBookingDate())
                .customerName(r.getCustomerName())
                .customerPhone(r.getCustomerPhone())
                .numberOfGuests(r.getNumberOfGuests())
                .timeSlot(r.getTimeSlot())
                .build();
    }
}