package com.tataki26.photoalbum.controller;

import com.tataki26.photoalbum.dto.AlbumDto;
import com.tataki26.photoalbum.service.AlbumService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AlbumController {
    private final AlbumService albumService;

    @GetMapping("/albums/{albumId}")
    public ResponseEntity<AlbumDto> getAlbum(@PathVariable("albumId") final Long id) {
        return new ResponseEntity<>(albumService.retrieveAlbumById(id), HttpStatus.OK);
    }
}
