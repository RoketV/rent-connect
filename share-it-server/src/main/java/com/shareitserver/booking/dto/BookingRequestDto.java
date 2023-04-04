package com.shareitserver.booking.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.Future;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class BookingRequestDto {
    private Long itemId;
    @Future
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime start;
    @Future
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime end;

    public BookingRequestDto(Long itemId, LocalDateTime start, LocalDateTime end) {
        this.itemId = itemId;
        this.start = start;
        this.end = end;
    }

    public BookingRequestDto() {
    }
}
