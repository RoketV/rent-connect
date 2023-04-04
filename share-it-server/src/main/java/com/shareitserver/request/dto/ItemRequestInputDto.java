package com.shareitserver.request.dto;

import com.shareitserver.user.model.User;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ItemRequestInputDto {

    private Long id;
    @NotBlank
    private String description;
    private User owner;
}
