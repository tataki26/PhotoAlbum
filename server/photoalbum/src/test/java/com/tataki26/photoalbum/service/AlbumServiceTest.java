package com.tataki26.photoalbum.service;

import com.tataki26.photoalbum.domain.Album;
import com.tataki26.photoalbum.dto.AlbumDto;
import com.tataki26.photoalbum.repository.AlbumRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.assertEquals;

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
        Album resultAlbum = albumService.retrieveAlbumById(savedAlbum.getId());

        assertEquals("test", resultAlbum.getName());
    }

    @Test
    void retrieveAlbumByName() {
        // insert album into persist context
        Album album = new Album();
        album.setName("test");
        Album savedAlbum = albumRepository.save(album);

        // get album from persist context
        Album resultAlbum = albumService.retrieveAlbumByName(savedAlbum.getName());

        assertEquals("test", resultAlbum.getName());
    }

}