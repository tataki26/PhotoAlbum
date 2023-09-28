package com.tataki26.photoalbum.domain;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.util.Date;

@Entity
@NoArgsConstructor
@Data
public class Album {
    @Id @GeneratedValue
    @Column(name = "album_id", unique = true, nullable = false)
    private Long id;
    @Column(name = "album_name", nullable = false)
    private String name;
    @CreationTimestamp
    private Date createdAt;
}
