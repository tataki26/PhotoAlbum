package com.tataki26.photoalbum.service;

import com.tataki26.photoalbum.Constants;
import com.tataki26.photoalbum.domain.Album;
import com.tataki26.photoalbum.domain.Photo;
import com.tataki26.photoalbum.dto.PhotoDto;
import com.tataki26.photoalbum.mapper.PhotoMapper;
import com.tataki26.photoalbum.repository.AlbumRepository;
import com.tataki26.photoalbum.repository.PhotoRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.imgscalr.Scalr;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
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

    public PhotoDto savePhoto(Long albumId, MultipartFile file) {
        // get album
        Optional<Album> albumOptional = albumRepository.findById(albumId);
        if (albumOptional.isEmpty()) {
            throw new EntityNotFoundException(String.format("ID %d로 조회된 앨범이 없습니다", albumId));
        }

        String originalFileName = file.getOriginalFilename();
        int fileSize = (int)file.getSize();
    }
}
