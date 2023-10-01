package com.tataki26.photoalbum.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.io.IOException;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(IOException.class)
    public ResponseEntity<String> handleIOException(IOException e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Internal Server Error: " + e.getMessage());
    }
}
