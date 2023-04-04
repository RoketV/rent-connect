package ru.practicum.booking.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;
import lombok.Data;
import ru.practicum.item.dto.ItemDto;
import ru.practicum.user.dto.UserDto;

import java.time.LocalDateTime;

@Data
public class BookingResponseDto {

    private Long id;
    private LocalDateTime start;
    private LocalDateTime end;
    @SerializedName("item")
    @JsonProperty("item")
    private ItemDto itemDto;
    @JsonProperty("booker")
    private UserDto user;
    @JsonProperty("status")
    private BookingState bookingState;
}
