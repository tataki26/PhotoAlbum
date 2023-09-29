package com.tataki26.photoalbum.service;

import com.tataki26.photoalbum.domain.Album;
import com.tataki26.photoalbum.dto.AlbumDto;
import com.tataki26.photoalbum.mapper.AlbumMapper;
import com.tataki26.photoalbum.repository.AlbumRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

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
}
