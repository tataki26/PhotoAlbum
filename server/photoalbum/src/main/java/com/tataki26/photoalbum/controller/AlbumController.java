package com.tataki26.photoalbum.controller;

import com.tataki26.photoalbum.dto.AlbumDto;
import com.tataki26.photoalbum.service.AlbumService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class AlbumController {
    private final AlbumService albumService;

    @GetMapping("/albums/{albumId}")
    public ResponseEntity<AlbumDto> getAlbum(@PathVariable("albumId") final Long id) {
        return new ResponseEntity<>(albumService.retrieveAlbumById(id), HttpStatus.OK);
    }

    @PostMapping("/albums")
    public ResponseEntity<AlbumDto> createAlbum(@RequestBody final AlbumDto albumDto) throws IOException {
        return new ResponseEntity<>(albumService.addNewAlbum(albumDto), HttpStatus.CREATED);
    }

    @GetMapping("/albums")
    public ResponseEntity<List<AlbumDto>> getAlbumList(@RequestParam(required = false, defaultValue = "byDate") final String sort,
                                                       @RequestParam(required = false) final String keyword) {
        return new ResponseEntity<>(albumService.retrieveAlbumList(sort, keyword), HttpStatus.OK);
    }
}
