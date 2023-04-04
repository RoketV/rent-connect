package com.shareitserver.booking.dto;

import com.google.gson.annotations.SerializedName;
import com.shareitserver.booking.enums.BookingState;
import com.shareitserver.item.model.Item;
import com.shareitserver.user.model.User;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class BookingResponseDto {

    private Long id;
    private LocalDateTime start;
    private LocalDateTime end;
    private Item item;
 //   @JsonProperty("booker")
    @SerializedName("booker")
    private User user;
  //  @JsonProperty("status")
    @SerializedName("status")
    private BookingState bookingState;
}
