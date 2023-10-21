package com.tataki26.photoalbum.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter @Setter
public class MemberDto {
    @JsonProperty("memberId")
    private Long id;
    // @Size(min = 6, max = 12, message = "아이디는 6자 이상, 12자 이하이어야 합니다")
    // @Pattern(regexp = "^[a-zA-Z0-9]*$", message = "아이디는 영문자와 숫자만 허용됩니다")
    @JsonProperty("memberName")
    private String name;
    @NotNull
    @Email
    private String email;
    @NotNull
    @Size(min = 6, max = 20, message = "비밀 번호는 6자 이상, 20자 이하이어야 합니다")
    @Pattern(regexp = "^(?!((?:[A-Za-z]+)|(?:[~!@#$%^&*()_+=]+)|(?:[0-9]+))$)[A-Za-z\\d~!@#$%^&*()_+=]{8,}$", message = "비밀번호는 영문자와 숫자, 특수문자를 2개 이상 조합하여 8자 이상이어야 합니다")
    private String password;
    private Date createdAt;
    private Date loginAt;
}
