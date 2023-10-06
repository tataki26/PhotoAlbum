package com.tataki26.photoalbum.controller;

import com.tataki26.photoalbum.dto.PhotoDto;
import com.tataki26.photoalbum.service.PhotoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("albums/{albumId}/photos")
@RequiredArgsConstructor
public class PhotoController {
    private final PhotoService photoService;

    @GetMapping("{photoId}")
    public ResponseEntity<PhotoDto> getPhoto(@PathVariable("albumId") final Long albumId,
                                             @PathVariable("photoId") final Long photoId) {
        return new ResponseEntity<>(photoService.retrievePhoto(albumId, photoId), HttpStatus.OK);
    }

}
