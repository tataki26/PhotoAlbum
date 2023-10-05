package com.tataki26.photoalbum.service;

import com.tataki26.photoalbum.domain.Album;
import com.tataki26.photoalbum.dto.AlbumDto;
import com.tataki26.photoalbum.repository.AlbumRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static com.tataki26.photoalbum.Constants.PATH_PREFIX;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class AlbumServiceTest {
    @Autowired
    AlbumRepository albumRepository;
    @Autowired
    AlbumService albumService;

    private static final String TEST_ALBUM_NAME = "test";

    Album savedAlbum;
    AlbumDto albumDto;

    @BeforeEach
    void setUp() {
        Album album = new Album(TEST_ALBUM_NAME);
        savedAlbum = albumRepository.save(album);

        albumDto = new AlbumDto();
        albumDto.setName(TEST_ALBUM_NAME);
    }

    @Test
    void retrieveAlbumById() {
        // get album from persist context
        AlbumDto albumDto = albumService.retrieveAlbumById(savedAlbum.getId());

        assertEquals(TEST_ALBUM_NAME, albumDto.getName());
    }

    @Test
    void retrieveAlbumByName() {
        // get album from persist context
        AlbumDto albumDto = albumService.retrieveAlbumByName(savedAlbum.getName());

        assertEquals(TEST_ALBUM_NAME, albumDto.getName());
    }

    @Test
    void failToRetrieveAlbumById() {
        assertThrows(EntityNotFoundException.class, () -> {
            albumService.retrieveAlbumById(-1L);
        });
    }

    @Test
    void failToRetrieveAlbumByName() {
        assertThrows(EntityNotFoundException.class, () -> {
            albumService.retrieveAlbumByName("none");
        });
    }

    @Test
    void addNewAlbum() throws IOException {
        AlbumDto resultDto = albumService.addNewAlbum(albumDto);
        assertEquals(albumDto.getName(), resultDto.getName());
    }

    @Test
    void addNewAlbumShouldCreateDirectories() throws IOException {
        AlbumDto resultDto = albumService.addNewAlbum(albumDto);

        assertTrue(Files.exists(Paths.get(PATH_PREFIX + "/photos/original/" + resultDto.getId())));
        assertTrue(Files.exists(Paths.get(PATH_PREFIX + "/photos/thumb/" + resultDto.getId())));
    }

    @Test
    void changeName() throws IOException {
        AlbumDto savedDto = albumService.addNewAlbum(albumDto);

        AlbumDto requestDto = new AlbumDto();
        requestDto.setName("changed");

        AlbumDto updatedAlbumDto = albumService.changeName(savedDto.getId(), requestDto);

        assertEquals("changed", updatedAlbumDto.getName());
    }

    @Test
    void changeNameWithInvalidName() throws IOException {
        AlbumDto savedDto = albumService.addNewAlbum(albumDto);

        AlbumDto requestDto = new AlbumDto();
        requestDto.setName("");

        assertThrows(IllegalArgumentException.class, () -> {
            albumService.changeName(savedDto.getId(), requestDto);
        });
    }

    @Test
    void removePhotosByAlbumId() throws IOException {
        AlbumDto savedDto = albumService.addNewAlbum(albumDto);
        albumService.removePhotosByAlbumId(savedDto.getId());

        assertThrows(EntityNotFoundException.class, () -> {
            albumService.retrieveAlbumById(savedDto.getId());
        });
    }
}