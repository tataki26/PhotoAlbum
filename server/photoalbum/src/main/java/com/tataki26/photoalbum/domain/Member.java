package com.tataki26.photoalbum.domain;

import com.tataki26.photoalbum.dto.MemberDto;
import com.tataki26.photoalbum.security.MemberRole;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Date;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Table(name="member")
public class Member {
    @Id @GeneratedValue
    @Column(name = "member_id", unique = true)
    private Long id;
    @Column(name = "member_name", nullable = false)
    private String name;
    @Email
    @Column(nullable = false)
    private String email;
    @Column(nullable = false)
    private String password;
    @CreationTimestamp
    private Date createdAt;
    private Date loginAt;
    @Enumerated(EnumType.STRING)
    private MemberRole role;
    @Enumerated(EnumType.STRING)
    private MemberStatus status;
    @CreationTimestamp
    private Date deactivatedAt;

    public void setLoginAt(Date loginAt) {
        this.loginAt = loginAt;
    }

    public void setStatus(MemberStatus status) {
        this.status = status;
    }

    public void setDeactivatedAt(Date deactivatedAt) {
        this.deactivatedAt = deactivatedAt;
    }

    public static Member createMember(MemberDto memberDto, PasswordEncoder encoder) {
        return Member.builder()
                .name(memberDto.getName())
                .email(memberDto.getEmail())
                .password(encoder.encode(memberDto.getPassword()))
                .build();
    }

    public void updatePassword(MemberDto memberDto, PasswordEncoder encoder) {
        this.password = encoder.encode(memberDto.getNewPassword());
    }
}
