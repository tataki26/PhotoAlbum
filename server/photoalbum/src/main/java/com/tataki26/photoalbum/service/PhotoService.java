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
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static com.tataki26.photoalbum.Constants.PATH_PREFIX;

@Service
@RequiredArgsConstructor
public class PhotoService {
    private final AlbumRepository albumRepository;
    private final PhotoRepository photoRepository;

    private final String ORIGINAL_PATH = PATH_PREFIX + "/photos/original";
    private final String THUMB_PATH = PATH_PREFIX + "/photos/thumb";

    private final List<String> ALLOWED_EXTENSIONS = Arrays.asList("jpg", "png", "jpeg");

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
        String ext = StringUtils.getFilenameExtension(originalFileName);
        int fileSize = (int)file.getSize();

        checkValidImageFile(file, ext);

        // set photo name after check if it already exists
        String checkedName = checkPhotoName(originalFileName, albumId);

        // save photo file into server
        savePhotoFile(file, albumId, checkedName);

        // save photo entity into db
        String originalUrl = "/photos/original/" + albumId + "/" + checkedName;
        String thumbUrl = "/photos/thumb/" + albumId + "/" + checkedName;
        // factory method
        Photo photo = Photo.createPhoto(checkedName, thumbUrl, originalUrl, fileSize, albumOptional.get());

        Photo savedPhoto = photoRepository.save(photo);

        return PhotoMapper.toDto(savedPhoto);
    }

    private String checkPhotoName(String originalFileName, Long albumId) {
        int count = 2;
        String newFileName = "";

        Optional<Photo> photoOptional = photoRepository.findByNameAndAlbum_Id(originalFileName, albumId);
        while (photoOptional.isPresent()) {
            newFileName = getNextPhotoName(originalFileName, count++);
            photoOptional = photoRepository.findByNameAndAlbum_Id(newFileName, albumId);
        }

        if (newFileName.isEmpty()) {
            newFileName = originalFileName;
        }

        return newFileName;
    }

    private String getNextPhotoName(String name, int count) {
        String photoName = StringUtils.stripFilenameExtension(name);
        String ext = StringUtils.getFilenameExtension(name);
        return String.format("%s(%d).%s", photoName, count, ext);
    }

    private void savePhotoFile(MultipartFile file, Long albumId, String fileName) {
        try {
            // save original photo file
            String filePath = albumId + "/" + fileName;
            Files.copy(file.getInputStream(), Paths.get(ORIGINAL_PATH + "/" + filePath));

            // resize thumb photo
            BufferedImage thumbImg = Scalr.resize(ImageIO.read(file.getInputStream()), Constants.THUMB_SIZE, Constants.THUMB_SIZE);

            // save resized thumb photo
            File thumbPhoto = new File(THUMB_PATH + "/" + filePath);

            String ext = StringUtils.getFilenameExtension(fileName);
            if (ext == null) {
                throw new IllegalArgumentException("유효하지 않은 확장자입니다");
            }

            // save thumb photo file
            ImageIO.write(thumbImg, ext, thumbPhoto);
        } catch (Exception e) {
            throw new RuntimeException("파일을 저장할 수 없습니다. 에러: " + e.getMessage());
        }
    }

    private void checkValidImageFile(MultipartFile file, String ext) {
        if (!ALLOWED_EXTENSIONS.contains(ext.toLowerCase())) {
            throw new IllegalArgumentException(String.format("%s는 지원하지 않는 확장자입니다", ext));
        }

        if (!isImageFile(file)) {
            throw new IllegalArgumentException("사진이 아닌 파일입니다");
        }
    }

    private boolean isImageFile(MultipartFile file) {
        try {
            byte[] buffer = file.getBytes();

            // check magic number
            // jpg/jpeg: 0xFFD8
            // png: 0x89504E470D0A1A0A
            if (buffer.length >= 2) {
                return (buffer[0] == (byte) 0xFF && (buffer[1] == (byte) 0xD8)// jpg
                        // png
                        || (buffer[0] == (byte) 0x89) &&
                        (buffer[1] == (byte) 0x50) &&
                        (buffer[2] == (byte) 0x4E) &&
                        (buffer[3] == (byte) 0x47) &&
                        (buffer[4] == (byte) 0x0D) &&
                        (buffer[5] == (byte) 0x0A) &&
                        (buffer[6] == (byte) 0x1A) &&
                        (buffer[7] == (byte) 0x0A));
            }

            return false;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public File retrievePhotoFile(Long id) {
        Optional<Photo> photoOptional = photoRepository.findById(id);
        if (photoOptional.isEmpty()) {
            throw new EntityNotFoundException(String.format("ID %d로 조회된 사진이 없습니다", id));
        }
        return new File(PATH_PREFIX + photoOptional.get().getOriginalUrl());
    }
}
