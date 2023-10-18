package com.tataki26.photoalbum.service;

import com.tataki26.photoalbum.dto.UserDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class UserServiceTest {
    @Autowired
    private UserService userService;

    @Test
    void addNewUser() {
        UserDto sampleDto = new UserDto();
        sampleDto.setName("test");
        sampleDto.setEmail("test@gmail.com");
        sampleDto.setPassword("temp_password");

        UserDto savedUserDto = userService.addNewUser(sampleDto);
        assertEquals(savedUserDto.getName(), sampleDto.getName());
    }

    @Test
    void failToAddNewUserWithEmptyValue() {
        UserDto sampleDto = new UserDto();
        assertThrows(IllegalArgumentException.class, () -> {
            userService.addNewUser(sampleDto);
        });
    }
}