package com.tataki26.photoalbum.domain;

import com.tataki26.photoalbum.dto.MemberDto;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.util.Date;

@Entity
@NoArgsConstructor
@Getter
@Table(name="member")
public class Member {
    @Id @GeneratedValue
    @Column(name = "member_id", unique = true)
    private Long id;
    @Column(name = "member_name", nullable = false)
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

    public static Member createMember(MemberDto memberDto) {
        String name = memberDto.getName();
        String email = memberDto.getEmail();
        String password = memberDto.getPassword();

        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("회원 이름은 생략할 수 없습니다.");
        }

        if (email == null || email.isEmpty()) {
            throw new IllegalArgumentException("회원 이메일은 생략할 수 없습니다.");
        }

        if (password == null || password.isEmpty()) {
            throw new IllegalArgumentException("비밀 번호는 생략할 수 없습니다.");
        }

        Member member = new Member();

        member.name = name;
        member.email = email;
        member.password = password;

        return member;
    }
}
