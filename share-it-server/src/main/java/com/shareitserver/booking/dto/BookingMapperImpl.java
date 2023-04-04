package com.shareitserver.booking.dto;

import com.shareitserver.booking.model.Booking;
import org.springframework.stereotype.Component;

@Component
public class BookingMapperImpl implements BookingMapper {

    @Override
    public Booking toBooking(BookingRequestDto dto) {
        if (dto == null) {
            return null;
        }
        Booking booking = new Booking();
        booking.setStart(dto.getStart());
        booking.setEnd(dto.getEnd());
        return booking;
    }

    @Override
    public BookingResponseDto toDto(Booking booking) {
        if (booking == null) {
            return null;
        }
        BookingResponseDto dto = new BookingResponseDto();
        dto.setId(booking.getId());
        dto.setItem(booking.getItem());
        dto.setBookingState(booking.getBookingState());
        dto.setUser(booking.getUser());
        dto.setStart(booking.getStart());
        dto.setEnd(booking.getEnd());
        return dto;
    }

    @Override
    public BookingInItemResponseDto toBookingInItemDto(Booking booking) {
        if (booking == null) {
            return null;
        }
        BookingInItemResponseDto dto = new BookingInItemResponseDto();
        dto.setId(booking.getId());
        dto.setStart(booking.getStart());
        dto.setEnd(booking.getEnd());
        dto.setBookerId(booking.getUser().getId());
        dto.setItemId(booking.getItem().getId());
        dto.setBookingState(booking.getBookingState());
        return dto;
    }
}
