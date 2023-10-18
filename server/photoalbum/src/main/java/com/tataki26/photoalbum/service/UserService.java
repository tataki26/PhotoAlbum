package com.tataki26.photoalbum.service;

import com.tataki26.photoalbum.domain.User;
import com.tataki26.photoalbum.dto.UserDto;
import com.tataki26.photoalbum.mapper.UserMapper;
import com.tataki26.photoalbum.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public UserDto addNewUser(UserDto userDto) {
        User user = User.createUser(userDto);
        User savedUser = userRepository.save(user);
        return UserMapper.toDto(savedUser);
    }
}
