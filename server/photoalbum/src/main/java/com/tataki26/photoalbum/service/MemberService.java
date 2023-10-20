package com.tataki26.photoalbum.service;

import com.tataki26.photoalbum.domain.Member;
import com.tataki26.photoalbum.dto.MemberDto;
import com.tataki26.photoalbum.mapper.MemberMapper;
import com.tataki26.photoalbum.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder encoder;

    public MemberDto addNewMember(MemberDto memberDto) {
        Member member = Member.createMember(memberDto, encoder);
        Member savedMember = memberRepository.save(member);
        return MemberMapper.toDto(savedMember);
    }
}
