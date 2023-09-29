package com.tataki26.photoalbum.mapper;

import com.tataki26.photoalbum.domain.Album;
import com.tataki26.photoalbum.dto.AlbumDto;

public class AlbumMapper {
    public static AlbumDto toDto(Album album) {
        AlbumDto albumDto = new AlbumDto();
        albumDto.setId(album.getId());
        albumDto.setName(album.getName());
        albumDto.setCreateAt(album.getCreatedAt());
        return albumDto;
    }
}
