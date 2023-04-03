package ru.practicum.booking.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class BookingInItemResponseDto {
    private Long id;
    private LocalDateTime start;
    private LocalDateTime end;
    private Long itemId;
    private Long bookerId;
    private BookingState bookingState;
}