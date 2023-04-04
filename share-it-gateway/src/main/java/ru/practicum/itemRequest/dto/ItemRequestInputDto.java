package ru.practicum.itemRequest.dto;


import lombok.Data;
import ru.practicum.user.dto.UserDto;

import javax.validation.constraints.NotBlank;

@Data
public class ItemRequestInputDto {

    private Long id;
    @NotBlank
    private String description;
    private UserDto owner;
}
