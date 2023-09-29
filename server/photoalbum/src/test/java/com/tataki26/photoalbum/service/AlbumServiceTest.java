package com.tataki26.photoalbum.service;

import com.tataki26.photoalbum.domain.Album;
import com.tataki26.photoalbum.dto.AlbumDto;
import com.tataki26.photoalbum.repository.AlbumRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@Transactional
class AlbumServiceTest {
    @Autowired
    AlbumRepository albumRepository;
    @Autowired
    AlbumService albumService;

    @Test
    void retrieveAlbumById() {
        // insert album into persist context
        Album album = new Album();
        album.setName("test");
        Album savedAlbum = albumRepository.save(album);

        // get album from persist context
        AlbumDto albumDto = albumService.retrieveAlbumById(savedAlbum.getId());

        assertEquals("test", albumDto.getName());
    }

    @Test
    void retrieveAlbumByName() {
        // insert album into persist context
        Album album = new Album();
        album.setName("test");
        Album savedAlbum = albumRepository.save(album);

        // get album from persist context
        AlbumDto albumDto = albumService.retrieveAlbumByName(savedAlbum.getName());

        assertEquals("test", albumDto.getName());
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

}