package com.tataki26.photoalbum.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter @Setter
public class AlbumDto {
    @JsonProperty("albumId")
    Long id;
    @JsonProperty("albumName")
    String name;
    Date createAt;
    int count;
}
