package com.shareitserver.item.dto;

import com.shareitserver.booking.dto.BookingInItemResponseDto;
import com.shareitserver.comments.dto.CommentOutputDto;
import com.shareitserver.user.model.User;
import lombok.Data;

import java.util.List;

@Data
public class ItemOutputDto {

    private Long id;
    private String name;
    private String description;
    private Boolean available;
    private BookingInItemResponseDto lastBooking;
    private BookingInItemResponseDto nextBooking;
    private User user;
    private Long requestId;
    private List<CommentOutputDto> comments;
}
