package ru.practicum.itemRequest.dto;

import lombok.Data;
import ru.practicum.item.dto.ItemOutputDto;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class ItemRequestOutputDto {

    private Long id;
    private String description;
    private LocalDateTime created;
    private List<ItemOutputDto> items;
}
