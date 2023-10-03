package com.tataki26.photoalbum.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Getter @Setter
public class AlbumDto {
    @JsonProperty("albumId")
    private Long id;
    @JsonProperty("albumName")
    private String name;
    private Date createAt;
    private int count;
    private List<String> thumbUrls;
}
