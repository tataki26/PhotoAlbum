package com.tataki26.photoalbum.mapper;

import com.tataki26.photoalbum.domain.Photo;
import com.tataki26.photoalbum.dto.PhotoDto;

public class PhotoMapper {
    public static PhotoDto toDto(Photo photo) {
        PhotoDto photoDto = new PhotoDto();
        photoDto.setId(photo.getId());
        photoDto.setName(photo.getName());
        photoDto.setUploadedAt(photo.getUploadedAt());
        photoDto.setThumbUrl(photo.getThumbUrl());
        photoDto.setOriginalUrl(photo.getOriginalUrl());
        return photoDto;
    }
}
