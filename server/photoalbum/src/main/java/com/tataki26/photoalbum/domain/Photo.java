package com.tataki26.photoalbum.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.util.Date;

import static jakarta.persistence.FetchType.*;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class Photo {
    @Id @GeneratedValue
    @Column(name = "photo_id", unique = true, nullable = false)
    private Long id;
    @Column(name = "photo_name", nullable = false)
    private String name;
    private String thumbUrl;
    private String originalUrl;
    private int fileSize;
    @CreationTimestamp
    private Date uploadedAt;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name="album_id")
    private Album album;

    public static Photo createPhoto(String name, String thumbUrl, String originalUrl, int fileSize, Album album) {
        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("사진 이름은 비어있을 수 없습니다.");
        }

        if (fileSize <= 0) {
            throw new IllegalArgumentException("파일 크기는 0보다 커야 합니다.");
        }

        Photo photo = new Photo();

        photo.name = name;
        photo.thumbUrl = thumbUrl;
        photo.originalUrl = originalUrl;
        photo.fileSize = fileSize;
        photo.album = album;

        return photo;
    }
}
