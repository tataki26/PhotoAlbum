package com.tataki26.photoalbum.controller;

import com.tataki26.photoalbum.dto.PhotoDto;
import com.tataki26.photoalbum.service.PhotoService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("albums/{albumId}/photos")
@RequiredArgsConstructor
public class PhotoController {
    private final PhotoService photoService;

    @GetMapping("{photoId}")
    public ResponseEntity<PhotoDto> getPhoto(@PathVariable("albumId") final Long albumId,
                                             @PathVariable("photoId") final Long photoId) {
        return new ResponseEntity<>(photoService.retrievePhoto(albumId, photoId), HttpStatus.OK);
    }

    @PostMapping("/v1")
    public ResponseEntity<List<PhotoDto>> uploadPhotosV1(@PathVariable("albumId") final Long albumId,
                                                         @RequestParam("photos")MultipartFile[] files) {
        List<PhotoDto> photoDtoList = new ArrayList<>();

        for (MultipartFile file : files) {
            PhotoDto photoDto = photoService.savePhotoV1(albumId, file);
            photoDtoList.add(photoDto);
        }

        return new ResponseEntity<>(photoDtoList, HttpStatus.OK);
    }

    @PostMapping("/v2")
    public ResponseEntity<List<PhotoDto>> uploadPhotosV2(@PathVariable("albumId") final Long albumId,
                                                         @RequestParam("photos") MultipartFile[] files) {
        List<PhotoDto> photoDtoList = new ArrayList<>();

        for (MultipartFile file : files) {
            PhotoDto photoDto = photoService.savePhotoV2(albumId, file);
            photoDtoList.add(photoDto);
        }

        return new ResponseEntity<>(photoDtoList, HttpStatus.OK);
    }

    @GetMapping("download/v1")
    public ResponseEntity<Void> downloadPhotosV1(@RequestParam("photoIds") Long[] photoIds, HttpServletResponse response) {
        try {
            File file;
            if (photoIds.length == 1) {
                file = photoService.retrievePhotoFile(photoIds[0]);
            } else {
                file = photoService.retrievePhotoFiles(photoIds);
            }
            OutputStream outputStream = response.getOutputStream();
            IOUtils.copy(new FileInputStream(file), outputStream);
            outputStream.close();
        } catch (FileNotFoundException e) {
            throw new RuntimeException("파일을 찾을 수 없습니다: ", e);
        } catch (IOException e) {
            throw new RuntimeException("파일을 다운로드 할 수 없습니다: ", e);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("download/v2")
    public ResponseEntity<InputStreamResource> downloadPhotosV2(@RequestParam("photoIds") Long[] photoIds) {
        try {
            InputStream is;
            HttpHeaders headers = new HttpHeaders();

            if (photoIds.length == 1) {
                is = photoService.retrievePhotoFileFromS3(photoIds[0]);
                String contentType = photoService.getContentTypeForFile(photoIds[0]);
                headers.setContentType(MediaType.parseMediaType(contentType));
            } else {
                is = photoService.retrievePhotoFilesFromS3(photoIds);
                headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
                headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=photos.zip");
            }

            return ResponseEntity
                    .status(HttpStatus.OK)
                    .headers(headers)
                    .body(new InputStreamResource(is));
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping
    public ResponseEntity<List<PhotoDto>> getPhotoList(@PathVariable("albumId") final Long id,
                                                       @RequestParam(required = false, defaultValue = "byDate") final String sort,
                                                       @RequestParam(required = false) final String keyword) {
        return new ResponseEntity<>(photoService.retrievePhotoList(id, sort, keyword), HttpStatus.OK);
    }

    @PutMapping("move/v1")
    public ResponseEntity<List<PhotoDto>> movePhotosV1(@RequestBody final PhotoDto photoDto) {
        return new ResponseEntity<>(photoService.movePhotosBetweenAlbumsV1(photoDto), HttpStatus.OK);
    }

    @PutMapping("move/v2")
    public ResponseEntity<List<PhotoDto>> movePhotosV2(@RequestBody final PhotoDto photoDto) {
        return new ResponseEntity<>(photoService.movePhotosBetweenAlbumsV2(photoDto), HttpStatus.OK);
    }

    @DeleteMapping("/v1")
    public ResponseEntity<Void> deletePhotoV1(@PathVariable("albumId") final Long albumId,
                                            @RequestBody final PhotoDto photoDto) {
        photoService.removePhotosV1(albumId, photoDto.getPhotoIds());
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping("/v2")
    public ResponseEntity<Void> deletePhotoV2(@PathVariable("albumId") final Long albumId,
                                              @RequestBody final PhotoDto photoDto) {
        photoService.removePhotosV2(albumId, photoDto.getPhotoIds());
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
