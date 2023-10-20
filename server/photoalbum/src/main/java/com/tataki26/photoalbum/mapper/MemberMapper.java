package com.tataki26.photoalbum.mapper;

import com.tataki26.photoalbum.domain.Member;
import com.tataki26.photoalbum.dto.MemberDto;

public class MemberMapper {
    public static MemberDto toDto(Member member) {
        MemberDto memberDto = new MemberDto();
        memberDto.setId(member.getId());
        memberDto.setName(member.getName());
        memberDto.setEmail(member.getEmail());
        memberDto.setPassword(member.getPassword());
        memberDto.setCreatedAt(member.getCreatedAt());
        memberDto.setLoginAt(member.getLoginAt());
        return memberDto;
    }
}
