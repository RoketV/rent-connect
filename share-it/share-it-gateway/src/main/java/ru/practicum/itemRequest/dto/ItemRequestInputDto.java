package ru.practicum.itemRequest.dto;


import lombok.Data;
import ru.practicum.user.User;

import javax.validation.constraints.NotBlank;

@Data
public class ItemRequestInputDto {

    private Long id;
    @NotBlank
    private String description;
    private User owner;
}
