package com.tataki26.photoalbum.repository;

import com.tataki26.photoalbum.domain.Photo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PhotoRepository extends JpaRepository<Photo, Long> {
    List<Photo> findTop4ByAlbum_IdOrderByUploadedAtDesc(Long id);
    int countByAlbum_Id(Long id);
    Optional<Photo> findByNameAndAlbum_Id(String photoName, Long albumId);
}
