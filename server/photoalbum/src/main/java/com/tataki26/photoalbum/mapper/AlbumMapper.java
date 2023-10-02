package com.tataki26.photoalbum.mapper;

import com.tataki26.photoalbum.domain.Album;
import com.tataki26.photoalbum.dto.AlbumDto;

import java.util.List;
import java.util.stream.Collectors;

public class AlbumMapper {
    public static AlbumDto toDto(Album album) {
        AlbumDto albumDto = new AlbumDto();
        albumDto.setId(album.getId());
        albumDto.setName(album.getName());
        albumDto.setCreateAt(album.getCreatedAt());
        return albumDto;
    }

    public static List<AlbumDto> toDtoList(List<Album> albumList) {
        return albumList.stream()
                        .map(AlbumMapper::toDto)
                        .collect(Collectors.toList());
    }
}
