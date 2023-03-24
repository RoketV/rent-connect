package com.shareitserver.booking.dto;

import com.shareitserver.booking.model.Booking;
import org.mapstruct.factory.Mappers;

public interface BookingMapper {

    BookingMapper BOOKING_MAPPER = Mappers.getMapper(BookingMapper.class);

    Booking toBooking(BookingRequestDto dto);

    BookingResponseDto toDto(Booking booking);

    BookingInItemResponseDto toBookingInItemDto(Booking booking);
}
