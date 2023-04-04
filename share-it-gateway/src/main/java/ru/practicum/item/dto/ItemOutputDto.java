package ru.practicum.item.dto;

import lombok.Data;
import ru.practicum.booking.dto.BookingInItemResponseDto;
import ru.practicum.comment.dto.CommentOutputDto;
import ru.practicum.user.User;

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