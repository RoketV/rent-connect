package ru.practicum.user.dto;

import com.fasterxml.jackson.annotation.JsonRootName;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Data
@JsonRootName("user")
public class UserDto {

    private Long id;
    @NotBlank
    private String name;
    @Email
    @NotBlank
    private String email;
    }