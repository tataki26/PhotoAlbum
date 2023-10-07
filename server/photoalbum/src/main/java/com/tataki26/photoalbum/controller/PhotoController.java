package com.tataki26.photoalbum.controller;

import com.tataki26.photoalbum.dto.PhotoDto;
import com.tataki26.photoalbum.service.PhotoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

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

    @PostMapping
    public ResponseEntity<List<PhotoDto>> uploadPhotos(@PathVariable("albumId") final Long albumId,
                                                       @RequestParam("photos")MultipartFile[] files) {
        List<PhotoDto> photoDtoList = new ArrayList<>();
        for (MultipartFile file : files) {
            PhotoDto photoDto = photoService.savePhoto(albumId, file);
            photoDtoList.add(photoDto);
        }

        return new ResponseEntity<>(photoDtoList, HttpStatus.OK);
    }

}
