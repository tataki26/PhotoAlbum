package com.tataki26.photoalbum.service;

import com.tataki26.photoalbum.domain.Album;
import com.tataki26.photoalbum.domain.Photo;
import com.tataki26.photoalbum.dto.AlbumDto;
import com.tataki26.photoalbum.dto.PhotoDto;
import com.tataki26.photoalbum.mapper.PhotoMapper;
import com.tataki26.photoalbum.repository.AlbumRepository;
import com.tataki26.photoalbum.repository.PhotoRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PhotoService {
    private final AlbumRepository albumRepository;
    private final PhotoRepository photoRepository;

    public PhotoDto retrievePhoto(Long albumId, Long photoId) {
        Optional<Album> albumOptional = albumRepository.findById(albumId);
        if (albumOptional.isEmpty()) {
            throw new EntityNotFoundException(String.format("ID %d로 조회된 앨범이 없습니다", albumId));
        }

        Album album = albumOptional.get();

        List<Photo> photos = album.getPhotos();
        for (Photo photo : photos) {
            if (photoId.equals(photo.getId())) {
                PhotoDto photoDto = PhotoMapper.toDto(photo);
                photoDto.setOriginalUrl(photo.getOriginalUrl());
                photoDto.setFileSize(photo.getFileSize());
                return photoDto;
            }
        }
        throw new EntityNotFoundException(String.format("ID %d로 조회된 사진이 없습니다", photoId));
    }
}
