package com.tataki26.photoalbum.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.util.Date;

import static jakarta.persistence.FetchType.*;

@Entity
@NoArgsConstructor
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
}
