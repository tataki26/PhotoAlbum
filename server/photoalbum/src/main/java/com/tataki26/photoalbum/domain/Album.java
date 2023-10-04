package com.tataki26.photoalbum.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.util.Date;
import java.util.List;

import static jakarta.persistence.CascadeType.*;

@Entity
@NoArgsConstructor
@Getter
public class Album {
    @Id @GeneratedValue
    @Column(name = "album_id", unique = true, nullable = false)
    private Long id;
    @Column(name = "album_name", nullable = false)
    private String name;
    @CreationTimestamp
    private Date createdAt;

    @OneToMany(mappedBy = "album", cascade = REMOVE)
    private List<Photo> photos;

    public Album(String name) {
        this.name = name;
    }

    public void setValidName(String name) {
        if (name != null && !name.isEmpty()) {
            this.name = name;
        } else {
            throw new IllegalArgumentException("유효하지 않은 이름입니다");
        }
    }
}
