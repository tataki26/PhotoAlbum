package com.tataki26.photoalbum.service;

import com.tataki26.photoalbum.domain.Album;
import com.tataki26.photoalbum.dto.AlbumDto;
import com.tataki26.photoalbum.mapper.AlbumMapper;
import com.tataki26.photoalbum.repository.AlbumRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static com.tataki26.photoalbum.Constants.PATH_PREFIX;

@Service
@RequiredArgsConstructor
public class AlbumService {
    private final AlbumRepository albumRepository;

    public AlbumDto retrieveAlbumById(Long id) {
        Optional<Album> albumOptional = albumRepository.findById(id);
        if (albumOptional.isPresent()) {
            return AlbumMapper.toDto(albumOptional.get());
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
        return AlbumMapper.toDtoList(albums);
    }
}
