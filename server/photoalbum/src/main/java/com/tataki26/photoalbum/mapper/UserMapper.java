package com.tataki26.photoalbum.mapper;

import com.tataki26.photoalbum.domain.Photo;
import com.tataki26.photoalbum.domain.User;
import com.tataki26.photoalbum.dto.PhotoDto;
import com.tataki26.photoalbum.dto.UserDto;

public class UserMapper {
    public static UserDto toDto(User user) {
        UserDto userDto = new UserDto();
        userDto.setId(user.getId());
        userDto.setName(user.getName());
        userDto.setEmail(user.getEmail());
        userDto.setPassword(user.getPassword());
        userDto.setCreatedAt(user.getCreatedAt());
        userDto.setLoginAt(user.getLoginAt());
        return userDto;
    }
}
