package ru.practicum.comment.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CommentOutputDto {
    private Long id;
    private String text;
    private String authorName;
    private LocalDateTime created;
}
