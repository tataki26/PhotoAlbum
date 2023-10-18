package com.tataki26.photoalbum.controller;

import com.tataki26.photoalbum.domain.User;
import com.tataki26.photoalbum.dto.UserDto;
import com.tataki26.photoalbum.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping
    public ResponseEntity<UserDto> createUser(@RequestBody final UserDto userDto) {
        return new ResponseEntity<>(userService.addNewUser(userDto), HttpStatus.CREATED);
    }
}
