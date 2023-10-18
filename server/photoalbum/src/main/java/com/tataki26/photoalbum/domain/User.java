package com.tataki26.photoalbum.domain;

import com.tataki26.photoalbum.dto.UserDto;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.validator.constraints.Email;

import java.util.Date;

@Entity
@NoArgsConstructor
@Getter
@Table(name="users")
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

    public static User createUser(UserDto userDto) {
        String name = userDto.getName();
        String email = userDto.getEmail();
        String password = userDto.getPassword();

        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("회원 이름은 생략할 수 없습니다.");
        }

        if (email == null || email.isEmpty()) {
            throw new IllegalArgumentException("회원 이메일은 생략할 수 없습니다.");
        }

        if (password == null || password.isEmpty()) {
            throw new IllegalArgumentException("비밀 번호는 생략할 수 없습니다.");
        }

        User user = new User();

        user.name = name;
        user.email = email;
        user.password = password;

        return user;
    }
}
