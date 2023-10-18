package com.tataki26.photoalbum.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter @Setter
public class UserDto {
    @JsonProperty("userId")
    private Long id;
    @JsonProperty("userName")
    private String name;
    private String email;
    private String password;
    private Date createdAt;
    private Date loginAt;
}
