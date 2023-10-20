package com.tataki26.photoalbum.service;

import com.tataki26.photoalbum.dto.MemberDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class MemberServiceTest {
    @Autowired
    private MemberService memberService;

    @Test
    void addNewUser() {
        MemberDto sampleDto = new MemberDto();
        sampleDto.setName("test");
        sampleDto.setEmail("test@gmail.com");
        sampleDto.setPassword("temp_password");

        MemberDto savedMemberDto = memberService.addNewMember(sampleDto);
        assertEquals(savedMemberDto.getName(), sampleDto.getName());
    }

    @Test
    void failToAddNewUserWithEmptyValue() {
        MemberDto sampleDto = new MemberDto();
        assertThrows(IllegalArgumentException.class, () -> {
            memberService.addNewMember(sampleDto);
        });
    }
}