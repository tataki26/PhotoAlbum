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

    AlbumDto sampleDto;
    boolean useAfter = true;

    @BeforeEach
    void setUp() throws IOException {
        AlbumDto requestSampleDto = new AlbumDto();
        requestSampleDto.setName(TEST_ALBUM_NAME);

        sampleDto = albumService.addNewAlbum(requestSampleDto);
    }

    @Test
    void retrieveAlbumById() {
        AlbumDto albumDto = albumService.retrieveAlbumById(sampleDto.getId());
        assertEquals(TEST_ALBUM_NAME, albumDto.getName());
    }

    @Test
    void retrieveAlbumByName() {
        AlbumDto albumDto = albumService.retrieveAlbumByName(sampleDto.getName());
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
            albumService.retrieveAlbumByName("");
        });
    }

    @Test
    void addNewAlbum() throws IOException {
        AlbumDto tempDto = new AlbumDto();
        tempDto.setName(TEST_ALBUM_NAME);

        AlbumDto resultDto = albumService.addNewAlbum(tempDto);
        assertEquals(tempDto.getName(), resultDto.getName());

        albumService.removePhotosByAlbumId(resultDto.getId());
    }

    @Test
    void addNewAlbumShouldCreateDirectories() throws IOException {
        AlbumDto tempDto = new AlbumDto();
        tempDto.setName(TEST_ALBUM_NAME);

        AlbumDto resultDto = albumService.addNewAlbum(tempDto);

        assertTrue(Files.exists(Paths.get(PATH_PREFIX + "/photos/original/" + resultDto.getId())));
        assertTrue(Files.exists(Paths.get(PATH_PREFIX + "/photos/thumb/" + resultDto.getId())));

        albumService.removePhotosByAlbumId(resultDto.getId());
    }

    @Test
    void changeName() throws IOException {
        AlbumDto requestDto = new AlbumDto();
        requestDto.setName("changed");

        AlbumDto updatedAlbumDto = albumService.changeName(sampleDto.getId(), requestDto);

        assertEquals("changed", updatedAlbumDto.getName());
    }

    @Test
    void changeNameWithInvalidName() throws IOException {
        AlbumDto requestDto = new AlbumDto();
        requestDto.setName("");

        assertThrows(IllegalArgumentException.class, () -> {
            albumService.changeName(sampleDto.getId(), requestDto);
        });
    }

    @Test
    void removePhotosByAlbumId() throws IOException {
        Long backupId = sampleDto.getId();
        albumService.removePhotosByAlbumId(backupId);

        assertThrows(EntityNotFoundException.class, () -> {
            albumService.retrieveAlbumById(backupId);
        });

        useAfter = false;
    }

    @AfterEach
    void afterEach() {
        if (useAfter) {
            albumService.removePhotosByAlbumId(sampleDto.getId());
        } else {
            useAfter = true;
        }
    }
}