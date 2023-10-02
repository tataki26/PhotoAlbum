package com.tataki26.photoalbum.service;

import com.tataki26.photoalbum.domain.Album;
import com.tataki26.photoalbum.dto.AlbumDto;
import com.tataki26.photoalbum.repository.AlbumRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

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

    @Test
    void retrieveAlbumById() {
        // insert album into persist context
        Album album = new Album(TEST_ALBUM_NAME);
        Album savedAlbum = albumRepository.save(album);

        // get album from persist context
        AlbumDto albumDto = albumService.retrieveAlbumById(savedAlbum.getId());

        assertEquals(TEST_ALBUM_NAME, albumDto.getName());
    }

    @Test
    void retrieveAlbumByName() {
        // insert album into persist context
        Album album = new Album(TEST_ALBUM_NAME);
        Album savedAlbum = albumRepository.save(album);

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
        AlbumDto albumDto = new AlbumDto();
        albumDto.setName(TEST_ALBUM_NAME);

        AlbumDto resultDto = albumService.addNewAlbum(albumDto);

        assertEquals(albumDto.getName(), resultDto.getName());
    }

    @Test
    void addNewAlbumShouldCreateDirectories() throws IOException {
        AlbumDto albumDto = new AlbumDto();
        albumDto.setName(TEST_ALBUM_NAME);

        AlbumDto resultDto = albumService.addNewAlbum(albumDto);

        assertTrue(Files.exists(Paths.get(PATH_PREFIX + "/photos/original/" + resultDto.getId())));
        assertTrue(Files.exists(Paths.get(PATH_PREFIX + "/photos/thumb/" + resultDto.getId())));
    }
}