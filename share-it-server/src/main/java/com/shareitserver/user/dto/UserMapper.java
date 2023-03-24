package com.shareitserver.user.dto;

import com.shareitserver.user.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

public interface UserMapper {


    UserMapper USER_MAPPER = Mappers.getMapper(UserMapper.class);

    UserDto toDto(User user);

    User toUser(UserDto dto);

}
