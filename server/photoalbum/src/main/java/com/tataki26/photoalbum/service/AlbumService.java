package com.tataki26.photoalbum.service;

import com.tataki26.photoalbum.domain.Album;
import com.tataki26.photoalbum.domain.Photo;
import com.tataki26.photoalbum.dto.AlbumDto;
import com.tataki26.photoalbum.mapper.AlbumMapper;
import com.tataki26.photoalbum.repository.AlbumRepository;
import com.tataki26.photoalbum.repository.PhotoRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

import static com.tataki26.photoalbum.Constants.PATH_PREFIX;

@Slf4j
@Service
@RequiredArgsConstructor
public class AlbumService {
    private final AlbumRepository albumRepository;
    private final PhotoRepository photoRepository;

    public AlbumDto retrieveAlbumById(Long id) {
        Optional<Album> albumOptional = albumRepository.findById(id);
        if (albumOptional.isPresent()) {
            AlbumDto albumDto = AlbumMapper.toDto(albumOptional.get());
            albumDto.setCount(photoRepository.countByAlbum_Id(id));
            return albumDto;
        } else {
            throw new EntityNotFoundException(String.format("ID %d로 조회된 앨범이 없습니다", id));
        }
    }

    public AlbumDto retrieveAlbumByName(String name) {
        Optional<Album> albumOptional = albumRepository.findByName(name);
        if (albumOptional.isPresent()) {
            return AlbumMapper.toDto(albumOptional.get());
        } else {
            throw new EntityNotFoundException(name + "(으)로 조회된 앨범이 없습니다");
        }
    }

    public AlbumDto addNewAlbum(AlbumDto albumDto) throws IOException {
        Album album = new Album(albumDto.getName());
        Album savedAlbum = albumRepository.save(album);
        createAlbumDirectories(savedAlbum);
        return AlbumMapper.toDto(savedAlbum);
    }

    private void createAlbumDirectories(Album album) throws IOException {
        Files.createDirectories(Paths.get(PATH_PREFIX + "/photos/original/" + album.getId()));
        Files.createDirectories(Paths.get(PATH_PREFIX + "/photos/thumb/" + album.getId()));
    }

    public List<AlbumDto> retrieveAlbumList(String sort, String keyword) {
        List<Album> albums;
        if (Objects.equals(sort, "byName")) {
            albums = albumRepository.findByNameContainingOrderByNameAsc(keyword);
        } else if (Objects.equals(sort, "byDate")) {
            albums = albumRepository.findByNameContainingOrderByCreatedAtDesc(keyword);
        } else {
            throw new IllegalArgumentException(sort + "(은)는 사용할 수 없는 정렬 기준입니다");
        }

        List<AlbumDto> albumDtos = AlbumMapper.toDtoList(albums);

        for (AlbumDto albumDto : albumDtos) {
            List<Photo> top4 = photoRepository.findTop4ByAlbum_IdOrderByUploadedAtDesc(albumDto.getId());
            albumDto.setThumbUrls(top4.stream()
                                      .map(Photo::getThumbUrl)
                                      .map(url -> PATH_PREFIX + url)
                                      .collect(Collectors.toList()));
        }

        return albumDtos;
    }

    public AlbumDto changeName(Long id, AlbumDto albumDto) {
        Optional<Album> albumOptional = albumRepository.findById(id);
        if (albumOptional.isEmpty()) {
            throw new NoSuchElementException(String.format("ID %d로 조회된 앨범이 없습니다", id));
        }

        Album album = albumOptional.get();
        album.setValidName(albumDto.getName());

        return AlbumMapper.toDto(albumRepository.save(album));
    }

    public void removeAlbum(Long id) {
        Album album = albumRepository.findById(id).orElse(null);
        if (album == null) {
            throw new NoSuchElementException(String.format("ID %d로 조회된 앨범이 없습니다", id));
        }
        deleteDirectory(PATH_PREFIX + "/photos/original/" + album.getId());
        deleteDirectory(PATH_PREFIX + "/photos/thumb/" + album.getId());
        albumRepository.deleteById(id);
    }

    private void deleteDirectory(String filePath) {
        try {
            File baseDir = new File(filePath);

            if (!baseDir.exists()) {
                return;
            }

            Files.walk(baseDir.toPath())
                    .sorted(Comparator.reverseOrder())
                    .map(Path::toFile)
                    .forEach((file) -> {
                        if (file.isDirectory()) {
                            log.info(file.getName() + " 폴더 삭제");
                        } else {
                            log.info(file.getName() + " 파일 삭제");
                        }
                        file.delete();
                    });
        } catch (IOException e) {
            log.error("파일 또는 디렉토리 삭제 중 오류 발생: " + e.getMessage());
        }
    }
}
