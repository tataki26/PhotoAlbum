package com.tataki26.photoalbum.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter @Setter
public class PhotoDto {
    @JsonProperty("photoId")
    private Long id;
    @JsonProperty("fileName")
    private String name;
    private String thumbUrl;
    private String originalUrl;
    private Date uploadedAt;
    private int fileSize;
}
