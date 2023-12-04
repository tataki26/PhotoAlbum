package com.tataki26.photoalbum.service;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.tataki26.photoalbum.Constants;
import com.tataki26.photoalbum.domain.Album;
import com.tataki26.photoalbum.domain.Photo;
import com.tataki26.photoalbum.dto.PhotoDto;
import com.tataki26.photoalbum.mapper.PhotoMapper;
import com.tataki26.photoalbum.repository.AlbumRepository;
import com.tataki26.photoalbum.repository.PhotoRepository;
import jakarta.persistence.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.imgscalr.Scalr;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import static com.tataki26.photoalbum.Constants.PATH_PREFIX;

@Slf4j
@Service
@RequiredArgsConstructor
public class PhotoService {
    private final AlbumRepository albumRepository;
    private final PhotoRepository photoRepository;

    private final AmazonS3Client amazonS3Client;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    private final String ORIGINAL_PATH = PATH_PREFIX + "/photos/original/";
    private final String THUMB_PATH = PATH_PREFIX + "/photos/thumb/";

    private final List<String> ALLOWED_EXTENSIONS = Arrays.asList("jpg", "png", "jpeg");

    @PersistenceContext
    private EntityManager entityManager;

    public PhotoDto retrievePhoto(Long albumId, Long photoId) {
        Album album = getAlbumById(albumId);

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

    private Album getAlbumById(Long albumId) {
        return albumRepository.findById(albumId)
                .orElseThrow(() -> new EntityNotFoundException(String.format("ID %d로 조회된 앨범이 없습니다", albumId)));
    }

    public PhotoDto savePhotoV1(Long albumId, MultipartFile file) {
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
        Photo photo = Photo.createPhoto(checkedName, thumbUrl, originalUrl, fileSize, getAlbumById(albumId));

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
            Files.copy(file.getInputStream(), Paths.get(ORIGINAL_PATH + filePath));

            // resize thumb photo
            BufferedImage thumbImg = Scalr.resize(ImageIO.read(file.getInputStream()), Constants.THUMB_SIZE, Constants.THUMB_SIZE);

            // save resized thumb photo
            File thumbPhoto = new File(THUMB_PATH + filePath);

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

    public List<String> savePhotoV2(MultipartFile[] files) {
        List<String> fileUrls = new ArrayList<>();
        try {
            for (MultipartFile file : files) {
                String fileName = file.getOriginalFilename();

                String fileUrl = "https://" + bucket + "/test" + fileName;
                fileUrls.add(fileUrl);

                ObjectMetadata metaData = new ObjectMetadata();
                metaData.setContentType(file.getContentType());
                metaData.setContentLength(file.getSize());

                amazonS3Client.putObject(bucket, fileName, file.getInputStream(), metaData);
            }
            return fileUrls;
        } catch (IOException e) {
            throw new RuntimeException("파일을 저장할 수 없습니다. 에러: " + e.getMessage());
        }
    }

    public File retrievePhotoFile(Long id) {
        Optional<Photo> photoOptional = photoRepository.findById(id);
        if (photoOptional.isEmpty()) {
            throw new EntityNotFoundException(String.format("ID %d로 조회된 사진이 없습니다", id));
        }
        return new File(PATH_PREFIX + photoOptional.get().getOriginalUrl());
    }

    public File retrievePhotoFiles(Long[] ids) throws IOException {
        List<String> filePaths = new ArrayList<>();
        for (Long id : ids) {
            Optional<Photo> photoOptional = photoRepository.findById(id);
            if (photoOptional.isEmpty()) {
                throw new EntityNotFoundException(String.format("ID %d로 조회된 사진이 없습니다", id));
            }
            String filePath = PATH_PREFIX + photoOptional.get().getOriginalUrl();
            filePaths.add(filePath);
        }
        return createZipFile(filePaths);
    }

    private File createZipFile(List<String> filePaths) throws IOException {
        // create zip file with name "download.zip"
        File zipFile = new File("download.zip");

        // stream to write data into file
        try (FileOutputStream fos = new FileOutputStream(zipFile);
             // stream to create zip file
             ZipOutputStream zos = new ZipOutputStream(fos)) {

            for (String filePath : filePaths) {
                File file = new File(filePath);
                FileInputStream fis = new FileInputStream(file);

                // add current file into zos
                // ZipEntry: file or directory in zip file
                zos.putNextEntry(new ZipEntry(file.getName()));
                byte[] buffer = new byte[1024];

                // length of data
                // -1 : EOF(End of File)
                int bytesRead;
                while ((bytesRead = fis.read(buffer)) != -1) {
                    // write fis data into zos
                    zos.write(buffer, 0, bytesRead);
                }

                // close stream
                zos.closeEntry();
                fis.close();
            }
        } catch (IOException e) {
            throw new IOException("zip 파일 생성에 실패했습니다: ", e);
        }
        return zipFile;
    }

    public List<PhotoDto> retrievePhotoList(Long id, String sort, String keyword) {
        List<Photo> photos;
        if (Objects.equals(sort, "byName")) {
            photos = photoRepository.findByAlbum_IdAndNameContainingOrderByNameAsc(id, keyword);
        } else if (Objects.equals(sort, "byDate")) {
            photos = photoRepository.findByAlbum_IdAndNameContainingOrderByUploadedAtDesc(id, keyword);
        } else {
            throw new IllegalArgumentException(sort + "(은)는 사용할 수 없는 정렬 기준입니다");
        }

        return PhotoMapper.toDtoList(photos);
    }

    @Transactional
    public List<PhotoDto> movePhotosBetweenAlbums(PhotoDto photoDto) {
        try {
            List<Long> photoIds = photoDto.getPhotoIds();
            Long fromAlbumId = photoDto.getFromAlbumId();
            Album fromAlbum = getAlbumById(fromAlbumId);
            Album toAlbum = getAlbumById(photoDto.getToAlbumId());

            movePhotoFiles(photoIds, fromAlbumId, toAlbum);

            updateToAlbumPhotos(photoIds, fromAlbum, toAlbum);
            deleteMovedPhotos(photoIds, fromAlbumId);

            return PhotoMapper.toDtoList(fromAlbum.getPhotos());
        } catch (Exception e) {
            throw new RuntimeException("사진 이동에 실패했습니다: " + e.getMessage(), e);
        }
    }

    private void movePhotoFiles(List<Long> photoIds, Long fromAlbumId, Album toAlbum) {
        for (Long photoId : photoIds) {
            Photo photo = photoRepository.findById(photoId)
                    .orElseThrow(() -> new EntityNotFoundException(String.format("ID %d로 조회된 사진이 없습니다", photoId)));
            String photoName = photo.getName();

            movePhotoFile(fromAlbumId, toAlbum.getId(), photoName, ORIGINAL_PATH);
            movePhotoFile(fromAlbumId, toAlbum.getId(), photoName, THUMB_PATH);
        }
    }

    private void movePhotoFile(Long fromAlbumId, Long toAlbumId, String photoName, String basePath) {
        String sourceFilePath = basePath + fromAlbumId + "/" + photoName;
        String targetFilePath = basePath + toAlbumId + "/" + photoName;

        try {
            Files.move(Paths.get(sourceFilePath), Paths.get(targetFilePath), StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new RuntimeException("파일 이동에 실패했습니다: " + e.getMessage(), e);
        }
    }

    private void updateToAlbumPhotos(List<Long> photoIds, Album fromAlbum, Album toAlbum) {
        entityManager.createQuery("UPDATE Photo p SET p.album = :toAlbum " +
                        "WHERE p.album = :fromAlbum AND p.id IN :photoIds")
                .setParameter("toAlbum", toAlbum)
                .setParameter("fromAlbum", fromAlbum)
                .setParameter("photoIds", photoIds)
                .executeUpdate();
    }

    private void deleteMovedPhotos(List<Long> photoIds, Long fromAlbumId) {
        entityManager.createQuery(
                        "DELETE FROM Photo p WHERE p.album.id = :albumId AND p.id IN :photoIds")
                .setParameter("albumId", fromAlbumId)
                .setParameter("photoIds", photoIds)
                .executeUpdate();
    }

    public void removePhotos(Long albumId, List<Long> photoIds) {
        for (Long photoId : photoIds) {
            Photo photo = photoRepository.findById(photoId)
                    .orElseThrow(() -> new EntityNotFoundException(String.format("ID %d로 조회된 사진이 없습니다", photoId)));

            if (photo.getAlbum().getId() != albumId) {
                throw new IllegalArgumentException(String.format("앨범에 사진 ID %d가 존재하지 않습니다", photoId));
            }

            String originalDirectoryPath = ORIGINAL_PATH + albumId + "/";
            String thumbDirectoryPath = THUMB_PATH + albumId + "/";

            if (deletePhotoFiles(originalDirectoryPath, photo.getName())
                    && deletePhotoFiles(thumbDirectoryPath, photo.getName())) {
                photoRepository.deleteById(photoId);
            }
        }
    }

    private boolean deletePhotoFiles(String directoryPath, String fileName) {
        File file = new File(directoryPath + fileName);

        if (file.exists() && file.isFile()) {
            if (file.delete()) {
                return true;
            } else {
                throw new RuntimeException("파일 삭제에 실패했습니다: " + file.getName());
            }
        }

        return false;
    }
}
