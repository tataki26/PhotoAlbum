package com.tataki26.photoalbum.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.validator.constraints.Email;

import java.util.Date;

@Entity
@NoArgsConstructor
@Getter
public class User {
    @Id @GeneratedValue
    @Column(name = "user_id", unique = true, nullable = false)
    private Long id;
    @Column(name = "user_name", nullable = false)
    private String name;
    @Email
    private String email;
    private String password;
    @CreationTimestamp
    private Date createdAt;
    private Date loginAt;

    public void setLoginAt(Date loginAt) {
        this.loginAt = loginAt;
    }
}
