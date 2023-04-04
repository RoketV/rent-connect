package com.shareitserver.comments.dto;

import com.shareitserver.comments.Comment;
import org.mapstruct.factory.Mappers;

public interface CommentMapper {

    CommentMapper COMMENT_MAPPER = Mappers.getMapper(CommentMapper.class);

    Comment toComment(CommentInputDto dto);

    CommentOutputDto toDto(Comment comment);

}
