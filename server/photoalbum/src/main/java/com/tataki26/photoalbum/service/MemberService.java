package com.tataki26.photoalbum.service;

import com.tataki26.photoalbum.domain.Member;
import com.tataki26.photoalbum.domain.MemberStatus;
import com.tataki26.photoalbum.dto.MemberDto;
import com.tataki26.photoalbum.mapper.MemberMapper;
import com.tataki26.photoalbum.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder encoder;
    private final TokenProvider tokenProvider;

    public MemberDto addNewMember(MemberDto memberDto) {
        Member member = Member.createMember(memberDto, encoder);
        Member savedMember = memberRepository.save(member);
        return MemberMapper.toDto(savedMember);
    }

    public MemberDto authenticateMember(MemberDto memberDto) {
        Member member = memberRepository.findByEmail(memberDto.getEmail())
                .filter(it -> encoder.matches(memberDto.getPassword(), it.getPassword()))
                .orElseThrow(() -> new IllegalArgumentException("아이디 또는 비밀번호가 일치하지 않습니다"));
        member.setLoginAt(new Date());

        MemberDto convertedDto = MemberMapper.toDto(member);
        String userSpecification = String.format("%s:%s", member.getId(), member.getRole());
        convertedDto.setToken(tokenProvider.createToken(userSpecification));

        return convertedDto;
    }

    public void changePassword(Long id, MemberDto memberDto) {
        Member member = memberRepository.findById(id)
                .filter(it -> encoder.matches(memberDto.getPassword(), it.getPassword()))
                .orElseThrow(() -> new IllegalArgumentException("비밀번호가 일치하지 않습니다"));
        member.updatePassword(memberDto, encoder);
    }

    public void changeMemberStatus(Long id, MemberDto memberDto) {
        Member member = memberRepository.findById(id)
                .filter(it -> encoder.matches(memberDto.getPassword(), it.getPassword()))
                .orElseThrow(() -> new IllegalArgumentException("비밀번호가 일치하지 않습니다"));
        member.setStatus(MemberStatus.INACTIVE);
        member.setDeactivatedAt(new Date());
    }
}
