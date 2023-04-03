package ru.practicum.comment.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class CommentInputDto {
    @NotBlank
    private String text;
}
