package com.tataki26.photoalbum.repository;

import com.tataki26.photoalbum.domain.Album;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@Transactional
class AlbumRepositoryTest {
    @Autowired
    AlbumRepository albumRepository;

    @Test
    void findByNameContainingOrderByNameAsc() throws InterruptedException {
        Album album1 = new Album("2023_eve");
        Album album2 = new Album("2023_adam");

        albumRepository.save(album1);
        TimeUnit.SECONDS.sleep(1);
        albumRepository.save(album2);

        List<Album> albumList = albumRepository.findByNameContainingOrderByNameAsc("2023");
        assertEquals(2, albumList.size());
        assertEquals("2023_adam", albumList.get(0).getName());
        assertEquals("2023_eve", albumList.get(1).getName());
    }

    @Test
    void findByNameContainingOrderByCreatedAtDesc() throws InterruptedException {
        Album album1 = new Album("2023_eve");
        Album album2 = new Album("2023_adam");

        albumRepository.save(album1);
        TimeUnit.SECONDS.sleep(1);
        albumRepository.save(album2);

        List<Album> albumList = albumRepository.findByNameContainingOrderByCreatedAtDesc("2023");
        assertEquals("2023_adam", albumList.get(0).getName());
        assertEquals("2023_eve", albumList.get(1).getName());
    }
}